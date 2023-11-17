package kr.co.promptech.noticeboard.feign;

import kr.co.promptech.noticeboard.config.feign.FeignHeaderConfig;
import kr.co.promptech.noticeboard.domain.model.feign.portal.ApiResponse;
import kr.co.promptech.noticeboard.domain.model.feign.portal.PortalRequestParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "MyFeignClientPortal",
        url = "https://api.odcloud.kr/api/GetSearchDataList/v1",
        configuration = FeignHeaderConfig.class
)
public interface MyFeignClientPortal {

    @PostMapping(value = "/searchData")
    ApiResponse getSearchData(@RequestParam("serviceKey") String serviceKey,
                              PortalRequestParam requestParam
    );
}
