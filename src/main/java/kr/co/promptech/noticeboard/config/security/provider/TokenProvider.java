package kr.co.promptech.noticeboard.config.security.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.promptech.noticeboard.constants.Constants;
import kr.co.promptech.noticeboard.domain.entity.Member;
import kr.co.promptech.noticeboard.domain.global.request.RefreshTokenRequest;
import kr.co.promptech.noticeboard.domain.vo.LoginVo;
import kr.co.promptech.noticeboard.enums.Role;
import kr.co.promptech.noticeboard.repository.MemberRepository;
import kr.co.promptech.noticeboard.service.auth.AuthService;
import kr.co.promptech.noticeboard.service.redis.RedisTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class TokenProvider {
    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final RedisTokenService redisTokenService;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
                         @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration, RedisTokenService redisTokenService, AuthService authService, MemberRepository memberRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisTokenService = redisTokenService;
        this.authService = authService;
        this.memberRepository = memberRepository;
    }

    // 토큰 생성
    public LoginVo generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        //Access Token 생성
        String accessToken = createAccessToken(authentication.getName(), authorities, now);

        // Refresh Token 생성
        String refreshToken = createRefreshToken(now);

        redisTokenService.saveRefreshToken(authentication.getName(), refreshToken, now.getTime() + refreshTokenExpiration);

        return new LoginVo(Constants.BEARER_PREFIX, accessToken, refreshToken, null, null);
    }

    public String createAccessToken(String subject, String authorities, Date nowDate) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(Constants.AUTH, authorities)
                .setExpiration(new Date(nowDate.getTime() + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Date nowDate) {
        return Jwts
                .builder()
                .setIssuedAt(nowDate)
                .setExpiration(new Date(nowDate.getTime() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(Constants.AUTH) == null) {
            throw new RuntimeException();
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Stream.of(claims.get(Constants.AUTH).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
//            handleExpiredToken(e);
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getMemberId(HttpServletRequest request) {
        String token = resolveToken(request);
        return getMemberId(token);
    }

    private Long getMemberId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.get("sub").toString());
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.BEARER_PREFIX)) {
            return bearerToken.substring(Constants.BEARER_PREFIX.length());
        }
        return null;
    }

    private void handleExpiredToken(ExpiredJwtException e) {
        String username = e.getClaims().getSubject();
        String refreshToken = redisTokenService.getRefreshToken(username);

        if (StringUtils.hasText(refreshToken) && validateToken(refreshToken)) {
            Date now = new Date();
            String sub = e.getClaims().getSubject();
            Member member = memberRepository.findById(Long.parseLong(sub)).orElseThrow(() ->
                    new IllegalArgumentException("유효하지 않은 회원입니다.")
            );

            Role role = Role.valueOf(member.getRole().roleName);
            String[] roleSplitList = role.roleList.split(",");
            List<String> trimRoleList = Arrays.stream(roleSplitList)
                    .map(r -> String.format("ROLE_%s", r.trim())).toList();
            String roleList = trimRoleList.toString().replace("[", "").replace("]", "")
                    .replace(" ", "");

            String accessToken = createAccessToken(String.valueOf(member.getId()),
                    roleList, now);

            Authentication authentication = getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("유효한 refresh token이 없습니다.");
        }
    }

    public String reIssueAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String expiredAccessToken = refreshTokenRequest.getExpiredAccessToken();
        Claims claims = parseClaims(expiredAccessToken);

        String sub = claims.get("sub").toString();
        Date now = new Date();

        Member member = memberRepository.findById(Long.parseLong(sub)).orElseThrow(() ->
                new IllegalArgumentException("유효하지 않은 회원입니다.")
        );

        redisTokenService.getRefreshToken(String.valueOf(member.getId()));

        Role role = Role.valueOf(member.getRole().roleName);
        String[] roleSplitList = role.roleList.split(",");
        List<String> trimRoleList = Arrays.stream(roleSplitList)
                .map(r -> String.format("ROLE_%s", r.trim())).toList();
        String roleList = trimRoleList.toString().replace("[", "").replace("]", "")
                .replace(" ", "");

        String accessToken = createAccessToken(String.valueOf(member.getId()),
                roleList, now);

        Authentication authentication = getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return accessToken;
    }


}
