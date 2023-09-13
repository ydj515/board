package kr.co.promptech.noticeboard.service.member;


import jakarta.servlet.http.HttpServletRequest;
import kr.co.promptech.noticeboard.config.security.provider.TokenProvider;
import kr.co.promptech.noticeboard.domain.dto.PatchMemberDto;
import kr.co.promptech.noticeboard.domain.entity.Member;
import kr.co.promptech.noticeboard.domain.vo.MemberVo;
import kr.co.promptech.noticeboard.domain.vo.MessageVo;
import kr.co.promptech.noticeboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;

    private Member getMember(HttpServletRequest request) {
        Long memberId = tokenProvider.getMemberId(request);
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원입니다."));
    }

    public MemberVo member(HttpServletRequest request) {
        Member findMember = getMember(request);

        return new MemberVo(findMember.getId(), findMember.getEmail(), findMember.getNickname(), findMember.getName(), findMember.getRole(), findMember.getSnsType(), findMember.getStatus(), findMember.getCreatedAt(), findMember.getModifiedAt());
    }

    @Transactional
    public MessageVo patchMember(PatchMemberDto patchMemberDto, HttpServletRequest request) {
        Member member = getMember(request);

        String birth = Optional.ofNullable(patchMemberDto.getBirth()).orElse("").replaceAll("-", "").trim();
        String name = Optional.ofNullable(patchMemberDto.getName()).orElse("").trim();
        String nickname = Optional.ofNullable(patchMemberDto.getNickname()).orElse("").trim();
        String password = Optional.ofNullable(patchMemberDto.getPassword()).orElse("").trim();
        if (!password.isEmpty()) {
            password = passwordEncoder.encode(password);
        }
        member.patchMember(birth, name, nickname, password);

        return new MessageVo("회원 정보 수정 성공");
    }
}
