package kr.co.promptech.noticeboard.config.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// application yml설정이랑 같이 해줘야 로그 보임
// 이렇게 설정하면 전역 설정이됨. 빼줄 것. 그리고 feign에 configuration = HeaderConfiguration.class 지정하는 방식으로!
@Configuration
public class FeignLogConfig {

    @Bean
    Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }

}
