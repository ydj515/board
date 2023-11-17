package kr.co.promptech.noticeboard.config.feign;

import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignRetryConfig {
    @Value("${feign.retry.period}")
    private long period;
    @Value("${feign.retry.max-period}")
    private long maxPeriod;
    @Value("${feign.retry.max-attempt}")
    private int maxAttempt;

    public FeignRetryConfig() {
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(this.period, this.maxPeriod, this.maxAttempt);
    }
}