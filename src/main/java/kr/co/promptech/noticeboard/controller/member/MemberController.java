package kr.co.promptech.noticeboard.controller.member;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.promptech.noticeboard.domain.global.request.PatchMemberRequest;
import kr.co.promptech.noticeboard.domain.global.base.Api;
import kr.co.promptech.noticeboard.domain.global.response.MemberResponse;
import kr.co.promptech.noticeboard.domain.global.base.MessageVo;
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
    public ResponseEntity<Api<MemberResponse>> member(HttpServletRequest request) {
        return ResponseEntity.ok(
                Api.<MemberResponse>builder()
                        .code(SUCCESS.code)
                        .message(SUCCESS.message)
                        .data(memberService.member(request))
                        .build()
        );
    }

    @PatchMapping
    public ResponseEntity<Api<MessageVo>> patchMember(
            @RequestBody @Validated PatchMemberRequest patchMemberRequest, HttpServletRequest request,
            BindingResult bindingResult) {
        return ResponseEntity.ok(
                Api.<MessageVo>builder()
                        .code(SUCCESS.code)
                        .message(SUCCESS.message)
                        .data(memberService.patchMember(patchMemberRequest, request))
                        .build()
        );
    }


}

