package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.xenia.CampaignTemplateEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignTemplateCustomObject {

    private Page <CampaignTemplateEntity> campaignTemplateDTOS;
    private long total;
}
