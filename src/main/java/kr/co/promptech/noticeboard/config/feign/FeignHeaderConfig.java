package kr.co.promptech.noticeboard.config.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignHeaderConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "Application/json");
//            requestTemplate.header("header2", "header value 2");
        };
    }
}