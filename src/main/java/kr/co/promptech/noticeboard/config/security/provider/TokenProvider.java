package kr.co.promptech.noticeboard.config.security.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.promptech.noticeboard.constants.Constants;
import kr.co.promptech.noticeboard.domain.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class TokenProvider {
    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    //    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;            // 30분
//    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;  // 30일

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
                         @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 토큰 생성
    public LoginVo generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + accessTokenExpiration);
        String accessToken = createAccessToken(authentication.getName(), authorities, accessTokenExpiresIn);

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new LoginVo(Constants.BEARER_PREFIX, accessToken, refreshToken, accessTokenExpiresIn.getTime(), null);
    }

    //Access Token 생성
    public String createAccessToken(String subject, String authorities, Date accessTokenExpiresIn) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(Constants.AUTH, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
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

}
