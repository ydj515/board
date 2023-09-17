package kr.co.promptech.noticeboard.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    MEMBER("MEMBER", "MEMBER"),
    ADMIN("ADMIN", "MEMBER, SELLER, ADMIN"),
    ;
    public final String roleName;
    public final String roleList;
}
