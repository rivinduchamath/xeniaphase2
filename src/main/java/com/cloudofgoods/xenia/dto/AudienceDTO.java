package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import com.cloudofgoods.xenia.models.NodeObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudienceDTO {

    private String audienceUuid;
    @NotEmptyOrNull(message = "Audience Name Must Not Be Empty")
    private String audienceName;
    @TextIndexed
    private String audienceDescription;
    private NodeObject audienceObject;
    @TextIndexed
    private String audienceRuleString;
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    private String organizationUuid;

}

