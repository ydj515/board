package kr.co.promptech.noticeboard.domain.model.feign.kstartup;

import lombok.Data;

@Data
public class ItemDetail {
    private String postsn;
    private String biztitle;
    private String supporttype;
    private String title;
    private String areaname;
    private String organizationname;
    private String posttarget;
    private String posttargetage;
    private String posttargetcomage;
    private String startdate;
    private String enddate;
    private String insertdate;
    private String viewcount;
    private String detailurl;
    private String prchCnadrNo;
    private String sprvInstClssCdNm;
    private String bizPrchDprtNm;
    private String blngGvdpCdNm;
}
