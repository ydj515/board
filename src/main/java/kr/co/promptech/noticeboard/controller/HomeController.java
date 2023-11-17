package kr.co.promptech.noticeboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    @RequestMapping(value = "")
    public String view() throws IOException {

        return "index";
    }
}
