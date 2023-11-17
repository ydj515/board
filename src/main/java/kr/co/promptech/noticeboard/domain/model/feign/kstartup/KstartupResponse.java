package kr.co.promptech.noticeboard.domain.model.feign.kstartup;

import lombok.Data;

@Data
public class KstartupResponse {
    private MyHeader header;
    private MyBody body;
}
