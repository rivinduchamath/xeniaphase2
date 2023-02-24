package com.cloudofgoods.xenia.service;


import com.cloudofgoods.xenia.dto.CampaignTemplateDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;


public interface CampaignTemplateService {
    ServiceResponseDTO saveOrUpdateTemplate(CampaignTemplateDTO ruleRootModel);

    ServiceResponseDTO getAllCampTemplatePagination(int page, int size);

    ServiceResponseDTO deleteTemplate(String templateId);
}
