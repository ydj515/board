package kr.co.promptech.noticeboard.domain.model.feign.portal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiResponse {
    private int statusCode;

    @JsonProperty("result")
    private Result result;

    @Data
    public static class Result {
        private int sum;
        private int dataCount;
        private List<DataItem> data;
    }

    @Data
    public static class DataItem {
        @JsonProperty("dataName")
        private String dataName;

        @JsonProperty("dataDescription")
        private String dataDescription;

        @JsonProperty("useScopeCode")
        private String useScopeCode;

        @JsonProperty("firstBrmName")
        private String firstBrmName;

        @JsonProperty("secondBrmName")
        private String secondBrmName;

        private String organization;

        @JsonProperty("coreDataName")
        private String coreDataName;

        private List<String> keywords;

        @JsonProperty("dataProvisionType")
        private String dataProvisionType;

        private List<String> extension;

        @JsonProperty("institutionType")
        private String institutionType;

        @JsonProperty("dataType")
        private String dataType;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("updateDate")
        private Date updateDate;

        @JsonProperty("detailPageUrl")
        private String detailPageUrl;

        private List<String> columns;

        @JsonProperty("coreData")
        private boolean isCoreData;

        @JsonProperty("corpApi")
        private boolean isCorpApi;
    }
}
