package kr.co.promptech.noticeboard.domain.global.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    private String expiredAccessToken;
    private String refreshToken;

}
