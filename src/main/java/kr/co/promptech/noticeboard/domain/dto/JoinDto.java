package kr.co.promptech.noticeboard.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinDto {

    @NotNull(message = "You can't be without email.")
    @Email(message = "Please correct the email format.", regexp = "^[A-Za-z0-9+_.-]+@(.+)$\n")
    private String email;
    @NotNull(message = "password cannot be empty.")
    private String password;
    @NotNull(message = "name cannot be empty.")
    private String name;
    @NotNull(message = "nickname cannot be empty.")
    private String nickname;
    private String birth;

    public void passwordEncoder(BCryptPasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }
}
