package kr.co.promptech.noticeboard.service.auth;

import kr.co.promptech.noticeboard.domain.entity.Member;
import kr.co.promptech.noticeboard.enums.Role;
import kr.co.promptech.noticeboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("회원 인증 처리");
        Member member = memberRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("유효하지 않은 회원입니다.")
        );

        Role role = member.getRole();
        String[] roles = role.getRoleList().split(",");

        return User.builder()
                .username(String.valueOf(member.getId()))
                .password(member.getPassword())
                .roles(roles)
                .build();
    }
}
