package kr.co.promptech.noticeboard.service.member;


import kr.co.promptech.noticeboard.config.security.provider.TokenProvider;
import kr.co.promptech.noticeboard.constants.Constants;
import kr.co.promptech.noticeboard.domain.global.request.JoinRequest;
import kr.co.promptech.noticeboard.domain.global.request.LoginRequest;
import kr.co.promptech.noticeboard.domain.entity.Member;
import kr.co.promptech.noticeboard.domain.global.request.RefreshTokenRequest;
import kr.co.promptech.noticeboard.domain.global.response.JoinResponse;
import kr.co.promptech.noticeboard.domain.global.response.LoginResponse;
import kr.co.promptech.noticeboard.repository.MemberRepository;
import kr.co.promptech.noticeboard.service.redis.RedisTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberAuthService {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;            // 30분

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisTokenService redisTokenService;

    @Transactional
    public JoinResponse join(JoinRequest joinRequest) {
        joinRequest.passwordEncoder(passwordEncoder);
        Optional<Member> findMember = memberRepository.findByEmail(joinRequest.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        Member saveMember = memberRepository.save(Member.of(joinRequest));
        return new JoinResponse(saveMember.getName(), saveMember.getNickname(), saveMember.getEmail());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // id, pw 기반으로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

        // security에 구현한 AuthService가 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authenticate);
    }

    public LoginResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        if (!tokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String reIssuedAccessToken = tokenProvider.reIssueAccessToken(refreshTokenRequest);

        return new LoginResponse(Constants.BEARER_PREFIX, reIssuedAccessToken, refreshTokenRequest.getRefreshToken(), null, null);
    }

    public LoginResponse authKakao(String code) {
        // TODO : kakao auth 구현
        return null;
    }

    @Transactional
    public LoginResponse loginKakao(String accessToken) {
        //TODO : kakao login 구현

        return null;
    }
}
