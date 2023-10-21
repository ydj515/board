package kr.co.promptech.noticeboard.domain.global;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDto {

    @NotNull(message = "point cannot be empty.")
    private String point;
    @NotNull(message = "details cannot be empty.")
    private String details;
}
