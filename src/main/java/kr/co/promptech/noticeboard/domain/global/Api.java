package kr.co.promptech.noticeboard.domain.global;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> {

    @NonNull
    private String code;
    @NonNull
    private String message;
    @NonNull
    private T data;
}
