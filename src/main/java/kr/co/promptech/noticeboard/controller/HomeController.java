package kr.co.promptech.noticeboard.controller;

import kr.co.promptech.noticeboard.service.feign.FeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final FeignService feignService;

    @RequestMapping(value = "")
    public String view() throws IOException {

        feignService.testFeign();
        return "index";
    }
}
