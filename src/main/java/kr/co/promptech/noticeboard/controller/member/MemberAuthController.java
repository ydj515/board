package kr.co.promptech.noticeboard.controller.member;

import kr.co.promptech.noticeboard.domain.dto.JoinDto;
import kr.co.promptech.noticeboard.domain.dto.LoginDto;
import kr.co.promptech.noticeboard.domain.global.Api;
import kr.co.promptech.noticeboard.domain.vo.JoinVo;
import kr.co.promptech.noticeboard.domain.vo.LoginVo;
import kr.co.promptech.noticeboard.service.member.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kr.co.promptech.noticeboard.enums.ResultCode.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/join")
    public ResponseEntity<Api<JoinVo>> join(@RequestBody @Validated JoinDto joinDto,
                                            BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Api.<JoinVo>builder()
                .code(JOIN_SUCCESS.code)
                .message(JOIN_SUCCESS.message)
                .data(memberAuthService.join(joinDto))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<Api<LoginVo>> login(@RequestBody @Validated LoginDto loginDto,
                                              BindingResult bindingResult) {
        return ResponseEntity.ok(
                Api.<LoginVo>builder()
                        .code(LOGIN_SUCCESS.code)
                        .message(LOGIN_SUCCESS.message)
                        .data(memberAuthService.login(loginDto))
                        .build()
        );
    }

    @GetMapping("/refresh/{refresh_token}")
    public ResponseEntity<Api<LoginVo>> refresh(
            @PathVariable(value = "refresh_token") String refreshToken) {
        return ResponseEntity.ok(
                Api.<LoginVo>builder()
                        .code(REFRESH_SUCCESS.code)
                        .message(REFRESH_SUCCESS.message)
                        .data(memberAuthService.refresh(refreshToken))
                        .build()
        );
    }
}
