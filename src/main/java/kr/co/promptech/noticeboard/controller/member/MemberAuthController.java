package kr.co.promptech.noticeboard.controller.member;

import kr.co.promptech.noticeboard.domain.global.base.Api;
import kr.co.promptech.noticeboard.domain.global.request.JoinRequest;
import kr.co.promptech.noticeboard.domain.global.request.LoginRequest;
import kr.co.promptech.noticeboard.domain.global.request.RefreshTokenRequest;
import kr.co.promptech.noticeboard.domain.global.response.JoinResponse;
import kr.co.promptech.noticeboard.domain.global.response.LoginResponse;
import kr.co.promptech.noticeboard.service.member.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.promptech.noticeboard.enums.ResultCode.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/join")
    public ResponseEntity<Api<JoinResponse>> join(@RequestBody @Validated JoinRequest joinRequest, BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Api.<JoinResponse>builder()
                .code(JOIN_SUCCESS.code)
                .message(JOIN_SUCCESS.message)
                .data(memberAuthService.join(joinRequest))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<Api<LoginResponse>> login(@RequestBody @Validated LoginRequest loginRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(
                Api.<LoginResponse>builder()
                        .code(LOGIN_SUCCESS.code)
                        .message(LOGIN_SUCCESS.message)
                        .data(memberAuthService.login(loginRequest))
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<Api<LoginResponse>> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(
                Api.<LoginResponse>builder()
                        .code(REFRESH_SUCCESS.code)
                        .message(REFRESH_SUCCESS.message)
                        .data(memberAuthService.refresh(refreshTokenRequest))
                        .build()
        );
    }

    // jwt token test용 url
    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
