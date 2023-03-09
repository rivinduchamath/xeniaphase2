package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.util.RuleStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignsObjects {
    @Indexed
    private String campaignName;
    private String campaignDescription;
    private String campaignId;
    private RuleStatus status;
    private Date createdDate;
    private Date endDate;
    private long totalRequestCount;
    private long totalResponseCount;
    private long matchResponses;
}
