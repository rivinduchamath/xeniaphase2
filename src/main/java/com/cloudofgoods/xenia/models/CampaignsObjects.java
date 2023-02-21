package com.cloudofgoods.xenia.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignsObjects {
    private String campaignName;
    private String status;
    private String createdDate;
    private String endDate;
    private long totalRequestCount;
    private long totalResponseCount;
    private long matchResponses;
}
