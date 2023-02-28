package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.CampaignTemplateDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.CampaignTemplateEntity;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.models.CampaignTemplateCustomObject;
import com.cloudofgoods.xenia.repository.CampaignTemplateRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.CampaignTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.cloudofgoods.xenia.util.Utils.*;
import static org.apache.log4j.varia.ExternallyRolledFileAppender.OK;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignTemplateServiceImpl implements CampaignTemplateService {

    private final CampaignTemplateRepository campaignTemplateRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateTemplate(CampaignTemplateDTO ruleRootModel) {
        log.info("LOG:: CampaignTemplateServiceImpl saveTemplate");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<CampaignTemplateEntity> campaignTemplateEntity;
            Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(ruleRootModel.getOrganizationUuid());
            if (organization.isPresent()) {
                if (NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getId())) {
                    log.info("LOG:: CampaignTemplateServiceImpl saveTemplate Update");
                    campaignTemplateEntity = campaignTemplateRepository.findById(ruleRootModel.getId());
                    if (campaignTemplateEntity.isPresent()) {
                        campaignTemplateEntity.get().setId(ruleRootModel.getId());
                        campaignTemplateEntity.get().setCreatedDate(ruleRootModel.getCreatedDate());
                        // Update
                        serviceResponseDTO.setData(campaignTemplateRepository.save(campaignTemplateEntitySetValues(campaignTemplateEntity.get(), ruleRootModel)));
                        serviceResponseDTO.setDescription("Update Template Success");
                    }
                } else {
                    CampaignTemplateEntity campaignTemplate;
                    log.info("LOG:: CampaignTemplateServiceImpl saveTemplate Save");
                    campaignTemplate = campaignTemplateEntitySetValues(new CampaignTemplateEntity(), ruleRootModel);
                    campaignTemplate.setCreatedDate(new Date().toString());
                    campaignTemplate.setCreator(ruleRootModel.getCreator());
                    // Save
                    serviceResponseDTO.setData(campaignTemplateRepository.save(campaignTemplate));
                    serviceResponseDTO.setDescription("Save Template Success");
                }
                serviceResponseDTO.setMessage(SUCCESS);
                serviceResponseDTO.setCode(STATUS_2000);
            } else {
                serviceResponseDTO.setDescription("Organization Not Found");
            }
        } catch (Exception exception) {
            log.info("LOG :: CampaignTemplateServiceImpl saveTemplate() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl saveTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getAllCampTemplatePagination(int page, int size) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: CampaignTemplateServiceImpl getAllCampTemplatePagination()");
        try {
            Page<CampaignTemplateEntity> campaignTemplateEntities = campaignTemplateRepository.findAll(PageRequest.of(page, size));
            long count = campaignTemplateRepository.count();

            CampaignTemplateCustomObject campaignTemplateCustomObject = new CampaignTemplateCustomObject();
            campaignTemplateCustomObject.setCampaignTemplateDTOS(campaignTemplateEntities);
            campaignTemplateCustomObject.setTotal(count);
            serviceResponseDTO.setDescription("Get AllCamp Template With Pagination Success");
            serviceResponseDTO.setData(campaignTemplateCustomObject);
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: CampaignTemplateServiceImpl getAllCampTemplatePagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl getAllCampTemplatePagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO deleteTemplate(String templateId) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG :: CampaignTemplateServiceImpl deleteTemplate()");
        try {
            campaignTemplateRepository.deleteById(templateId);
            serviceResponseDTO.setMessage("Delete Template Success");
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: CampaignTemplateServiceImpl deleteTemplate() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("CampaignTemplateServiceImpl deleteTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(OK);
        return serviceResponseDTO;
    }

    CampaignTemplateEntity campaignTemplateEntitySetValues(CampaignTemplateEntity campaignTemplateEntity, CampaignTemplateDTO ruleRootModel) {
        if (NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getCampaignDescription()))
            campaignTemplateEntity.setCampaignDescription(ruleRootModel.getCampaignDescription());
        campaignTemplateEntity.setCampaignName(ruleRootModel.getCampaignName());
        campaignTemplateEntity.setOrganization(ruleRootModel.getOrganizationUuid());
        if (NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getUpdater()))
            campaignTemplateEntity.setUpdater(ruleRootModel.getUpdater());
        if (NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getStartDateTime()))
            campaignTemplateEntity.setStartDateTime(ruleRootModel.getStartDateTime());
        if (NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getEndDateTime()))
            campaignTemplateEntity.setEndDateTime(ruleRootModel.getEndDateTime());
        campaignTemplateEntity.setChannels(ruleRootModel.getChannels());
        if (NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getTags()))
            campaignTemplateEntity.setTags(ruleRootModel.getTags());
        if (NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getChannels()))
            campaignTemplateEntity.setChannels(ruleRootModel.getChannels());
        if (NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getChannelIds()))
            campaignTemplateEntity.setChannelIds(ruleRootModel.getChannelIds());
        return campaignTemplateEntity;
    }

}
