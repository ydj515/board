package kr.co.promptech.noticeboard.service.feign;

import kr.co.promptech.noticeboard.domain.model.feign.kstartup.KstartupApiResponse;
import kr.co.promptech.noticeboard.domain.model.feign.kstartup.KstartupRequestParam;
import kr.co.promptech.noticeboard.domain.model.feign.portal.ApiResponse;
import kr.co.promptech.noticeboard.domain.model.feign.portal.PortalRequestParam;
import kr.co.promptech.noticeboard.feign.MyFeignClientKstartup;
import kr.co.promptech.noticeboard.feign.MyFeignClientPortal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeignService {

    private final MyFeignClientKstartup feignClientKstartup;
    private final MyFeignClientPortal feignClientPortal;
    @Value("${servicekey}")
    private String serviceKey;

    public void testFeign() {

        KstartupRequestParam kstartupRequestParam = KstartupRequestParam.builder()
                .page(1)
                .perPage(10)
                .serviceKey(serviceKey)
                .returnType("json")
                .reginClss("전라북도")
                .cntrNm("한국농수산대학 창업보육센터")
                .build();

        KstartupApiResponse resultkstartup = feignClientKstartup.getCenterList(
                kstartupRequestParam
        );

        PortalRequestParam param = PortalRequestParam.builder()
                .page(1)
                .size(10)
                .dataType(List.of("FILE", "API", "STD"))
                .build();
        ApiResponse apiResponse = feignClientPortal.getSearchData(
                serviceKey,
                param
        );

        System.out.println();


    }
}
