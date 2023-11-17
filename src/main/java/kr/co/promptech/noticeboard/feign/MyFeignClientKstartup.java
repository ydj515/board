package kr.co.promptech.noticeboard.feign;

import kr.co.promptech.noticeboard.config.feign.FeignHeaderConfig;
import kr.co.promptech.noticeboard.config.feign.FeignRetryConfig;
import kr.co.promptech.noticeboard.domain.model.feign.kstartup.FeignResultkstartup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "MyFeignClientKstartup",
        url = "${external.service.host}",
        configuration = {FeignHeaderConfig.class, FeignRetryConfig.class}
)
public interface MyFeignClientKstartup {

    @GetMapping(value = "/getAnnouncementList")
    FeignResultkstartup getAnnouncementList(@RequestParam("serviceKey") String serviceKey,
                                            @RequestParam("pageNo") int pageNo,
                                            @RequestParam("numOfRows") int numOfRows,
                                            @RequestParam("startDate") String startDate,
                                            @RequestParam("endDate") String endDate,
                                            @RequestParam("openYn") String openYn,
                                            @RequestParam("dataType") String dataType
    );
}
