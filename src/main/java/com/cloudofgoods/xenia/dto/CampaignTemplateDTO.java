package com.cloudofgoods.xenia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignTemplateDTO {

    private String id;
    public String campaignDescription;
    public String campaignName;
    public String createdDate;
    public String creator;
    public String endDateTime;
    public String organization;
    public String slotId;
    public int priority;
    public String startDateTime;
    public String updater;
    public String campTemplateName;
}
