package kr.co.promptech.noticeboard.feign;

import kr.co.promptech.noticeboard.config.feign.FeignHeaderConfig;
import kr.co.promptech.noticeboard.config.feign.FeignRetryConfig;
import kr.co.promptech.noticeboard.domain.model.feign.kstartup.KstartupApiResponse;
import kr.co.promptech.noticeboard.domain.model.feign.kstartup.KstartupRequestParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "MyFeignClientKstartup",
        url = "${external.service.host}",
        configuration = {FeignHeaderConfig.class, FeignRetryConfig.class}
)
public interface MyFeignClientKstartup {

    @GetMapping(value = "/kisedSlpService/getCenterList")
    KstartupApiResponse getCenterList(@SpringQueryMap KstartupRequestParam kstartupRequestParam
    );
}
