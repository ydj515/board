package kr.co.promptech.noticeboard.domain.global.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.co.promptech.noticeboard.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchMemberRequest {

    private String birth;
    private String name;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Status status;
}
