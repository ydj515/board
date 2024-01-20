package kr.co.promptech.noticeboard.controller;

import kr.co.promptech.noticeboard.service.feign.FeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final FeignService feignService;

    @GetMapping("")
    public String index(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/sign-in";
        }
        return "pages/dashboard";
    }
}
