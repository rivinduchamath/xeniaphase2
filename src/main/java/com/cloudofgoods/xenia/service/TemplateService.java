package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.TemplateEntity;


import java.util.concurrent.ExecutionException;

public interface TemplateService {

    ServiceResponseDTO getAllTemplate();

    ServiceResponseDTO deleteTemplateById(String templateId) throws ExecutionException, InterruptedException;

    ServiceResponseDTO getAllTemplatePagination(int page, int size);

    ServiceResponseDTO  saveTemplate(TemplateEntity ruleRootModel);

    ServiceResponseDTO getTemplateById(String templateId);

    ServiceResponseDTO getTemplateByName(String name);
}