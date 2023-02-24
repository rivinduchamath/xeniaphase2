package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.util.RuleStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignsObjects {
    private String campaignName;
    private String campaignDescription;
    private String campaignId;
    private RuleStatus status;
    private String createdDate;
    private String endDate;
    private long totalRequestCount;
    private long totalResponseCount;
    private long matchResponses;
}
