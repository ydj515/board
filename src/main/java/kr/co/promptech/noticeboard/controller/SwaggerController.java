package kr.co.promptech.noticeboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/swagger")
public class SwaggerController {

    @GetMapping
    public String redirectSwaggerUi() {
        return "redirect:/swagger-ui/index.html";
    }
}
