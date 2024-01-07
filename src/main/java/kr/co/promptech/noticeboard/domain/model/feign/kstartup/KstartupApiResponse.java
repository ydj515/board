package kr.co.promptech.noticeboard.domain.model.feign.kstartup;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class KstartupApiResponse {

    private int currentCount;
    private List<ApiData> data;
    private int matchCount;
    private int page;
    private int perPage;
    private int totalCount;

    @Getter
    @Setter
    @Builder
    public static class ApiData {
        private String addr;
        private int buld_id;
        private String buld_nm;
        private int cntr_id;
        private String cntr_intrd_type_nm;
        private String cntr_nm;
        private String cntr_type_nm;
        private String hmpg;
        private String latde;
        private String lgtde;
        private String pstno;
        private String regin_clss;
        private int spce_cnt;
    }
}
