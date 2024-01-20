package kr.co.promptech.noticeboard.controller.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
class AuthViewController {

    @GetMapping("/sign-in")
    public String singIn(Model model) {

        return "pages/auth/login";
    }

    @GetMapping("/sign-up")
    public String singUp() {
        return "pages/auth/register";
    }

}