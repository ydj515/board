package kr.co.promptech.noticeboard.domain.global;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDto {

    @NotNull(message = "location cannot be empty.")
    private String location;
    @NotNull(message = "details cannot be empty.")
    private String details;
}
