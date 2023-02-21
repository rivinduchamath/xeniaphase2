package com.cloudofgoods.xenia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudienceDTO {
    @Id
    private String audienceUuid;
    private String audienceName;
    @TextIndexed
    private String audienceDescription;
    @TextIndexed
    private String audienceJson;
    @TextIndexed
    private String audienceRuleString;
    private String channelUuid;
    private String organizationUuid;
    private String campaignUuid;
}

