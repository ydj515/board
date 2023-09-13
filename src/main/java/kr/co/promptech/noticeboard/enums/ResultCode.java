package kr.co.promptech.noticeboard.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
    SUCCESS("0", "success"),
    JOIN_SUCCESS("1", "join success"),
    LOGIN_SUCCESS("2", "login success"),
    REFRESH_SUCCESS("3", "refresh token success"),
    FAIL("99", "99 fail"),
    ;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code;
    public String message;
}
