package kr.co.promptech.noticeboard.domain.global.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kr.co.promptech.noticeboard.enums.Role;
import kr.co.promptech.noticeboard.enums.SnsJoinType;
import kr.co.promptech.noticeboard.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResponse {
    Long memberId;
    String email;
    String nickname;
    String name;
    Role role;
    SnsJoinType snsType;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
}
