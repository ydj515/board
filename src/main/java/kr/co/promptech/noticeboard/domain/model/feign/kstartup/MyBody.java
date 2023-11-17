package kr.co.promptech.noticeboard.domain.model.feign.kstartup;

import lombok.Data;

import java.util.List;

@Data
public class MyBody {
    private String pageNo;
    private String totalCount;
    private List<MyItem> items;
    private String numOfRows;
}
