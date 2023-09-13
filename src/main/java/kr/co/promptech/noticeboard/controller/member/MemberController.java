package kr.co.promptech.noticeboard.controller.member;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.promptech.noticeboard.domain.dto.PatchMemberDto;
import kr.co.promptech.noticeboard.domain.global.Api;
import kr.co.promptech.noticeboard.domain.vo.MemberVo;
import kr.co.promptech.noticeboard.domain.vo.MessageVo;
import kr.co.promptech.noticeboard.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kr.co.promptech.noticeboard.enums.ResultCode.SUCCESS;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Api<MemberVo>> member(HttpServletRequest request) {
        return ResponseEntity.ok(
                Api.<MemberVo>builder()
                        .code(SUCCESS.code)
                        .message(SUCCESS.message)
                        .data(memberService.member(request))
                        .build()
        );
    }

    @PatchMapping
    public ResponseEntity<Api<MessageVo>> patchMember(
            @RequestBody @Validated PatchMemberDto patchMemberDto, HttpServletRequest request,
            BindingResult bindingResult) {
        return ResponseEntity.ok(
                Api.<MessageVo>builder()
                        .code(SUCCESS.code)
                        .message(SUCCESS.message)
                        .data(memberService.patchMember(patchMemberDto, request))
                        .build()
        );
    }


}

