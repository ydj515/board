package kr.co.promptech.noticeboard.domain.model.feign.kstartup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KstartupRequestParam {
    private String serviceKey;
    private int page;
    private int perPage;
    private String returnType;
    private String cntrNm;
    private String reginClss;
}
