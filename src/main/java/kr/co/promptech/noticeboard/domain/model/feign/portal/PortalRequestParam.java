package kr.co.promptech.noticeboard.domain.model.feign.portal;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PortalRequestParam {
    private final int page;

    private final int size;

    private List<String> dataType;
}
