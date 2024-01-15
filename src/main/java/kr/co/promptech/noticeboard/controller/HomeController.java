package kr.co.promptech.noticeboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.promptech.noticeboard.service.feign.FeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final FeignService feignService;

    @RequestMapping(value = "/")
    public String view(HttpServletRequest request, Authentication authentication) throws IOException {

        return "redirect:/dashboard/collect";
    }

    /**
     * 로그인 화면
     *
     * @return
     */
    @GetMapping("/sign-in")
    public String login(HttpServletRequest request, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard/collect";
        }

        return "login/sign-in";
    }
}
