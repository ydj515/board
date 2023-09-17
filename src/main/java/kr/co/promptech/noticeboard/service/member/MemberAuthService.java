package kr.co.promptech.noticeboard.service.member;


import io.jsonwebtoken.Claims;
import kr.co.promptech.noticeboard.config.security.provider.TokenProvider;
import kr.co.promptech.noticeboard.constants.Constants;
import kr.co.promptech.noticeboard.domain.dto.JoinDto;
import kr.co.promptech.noticeboard.domain.dto.LoginDto;
import kr.co.promptech.noticeboard.domain.entity.Member;
import kr.co.promptech.noticeboard.domain.vo.JoinVo;
import kr.co.promptech.noticeboard.domain.vo.LoginVo;
import kr.co.promptech.noticeboard.enums.Role;
import kr.co.promptech.noticeboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberAuthService implements UserDetailsService {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;            // 30분

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Transactional
    public JoinVo join(JoinDto joinDto) {
        joinDto.passwordEncoder(passwordEncoder);
        Optional<Member> findMember = memberRepository.findByEmail(joinDto.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        Member saveMember = memberRepository.save(Member.of(joinDto));
        return new JoinVo(saveMember.getName(), saveMember.getNickname(), saveMember.getEmail());
    }

    public LoginVo login(LoginDto loginDto) {
        // id, pw 기반으로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // security에 구현한 AuthService가 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authenticate);
    }

    public LoginVo refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // TODO :: origin access token get
        String originAccessToken = "";
        Claims claims = tokenProvider.parseClaims(originAccessToken);

        String sub = claims.get("sub").toString();
        Date accessTokenExpired = new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        Member member = memberRepository.findById(Long.parseLong(sub)).orElseThrow(() ->
                new IllegalArgumentException("유효하지 않은 회원입니다.")
        );

        Role role = Role.valueOf(member.getRole().roleName);
        String[] roleSplitList = role.roleList.split(",");
        List<String> trimRoleList = Arrays.stream(roleSplitList)
                .map(r -> String.format("ROLE_%s", r.trim())).toList();
        String roleList = trimRoleList.toString().replace("[", "").replace("]", "")
                .replace(" ", "");

        String accessToken = tokenProvider.createAccessToken(String.valueOf(member.getId()),
                roleList, accessTokenExpired);

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new LoginVo(Constants.BEARER_PREFIX, accessToken, refreshToken, accessTokenExpired.getTime(), null);
    }

    public LoginVo authKakao(String code) {
        // TODO : kakao auth 구현
        return null;
    }

    @Transactional
    public LoginVo loginKakao(String accessToken) {
        //TODO : kakao login 구현

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("회원 인증 처리");

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 회원입니다."));

        Role role = member.getRole();
        String[] roles = role.getRoleList().split(",");

        return User.builder()
                .username(String.valueOf(member.getId()))
                .password(member.getPassword())
                .roles(roles)
                .build();
    }
}
