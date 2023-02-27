package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import com.cloudofgoods.xenia.models.RuleChannelObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CampaignTemplateDTO implements Serializable {

    private String id;
    public String campaignDescription;
    @NotEmptyOrNull(message = "Campaign Name Must Not Be Empty")
    public String campaignName;
    public String createdDate;
    public String creator;
    public String endDateTime;
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    public String organizationUuid;
    public String startDateTime;
    public String campTemplateName;
    public String updater;
    private List <String> channelIds;
    private List <String> tags;
    @NotEmptyOrNull(message = "Channels Objects Must Not Be Empty")
    private List <RuleChannelObject> channels;

}
