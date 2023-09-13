package kr.co.promptech.noticeboard.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    MEMBER("MEMBER", "MEMBER"),
    ADMIN("ADMIN", "MEMBER, SELLER, ADMIN"),
    ;
    public final String roleName;
    public final String roleList;
}
