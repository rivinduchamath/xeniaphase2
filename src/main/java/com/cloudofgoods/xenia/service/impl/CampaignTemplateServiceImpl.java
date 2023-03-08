package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.CampaignTemplateDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.CampaignTemplateEntity;
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
import java.util.concurrent.CompletableFuture;

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
            organizationRepository.findByUuidEquals(ruleRootModel.getOrganizationUuid())
                    .ifPresentOrElse(
                            organization -> {
                                if (NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getId())) {
                                    log.info("LOG:: CampaignTemplateServiceImpl saveTemplate Update");
                                    campaignTemplateRepository.findByIdAndOrganizationEquals(ruleRootModel.getId(),ruleRootModel.getOrganizationUuid())
                                            .ifPresentOrElse(
                                                    entity -> {
                                                        entity.setId(ruleRootModel.getId());
                                                        entity.setCreatedDate(ruleRootModel.getCreatedDate());
                                                        serviceResponseDTO.setData(campaignTemplateRepository.save(campaignTemplateEntitySetValues(entity, ruleRootModel)));
                                                        serviceResponseDTO.setDescription("Update Template Success");
                                                    },
                                                    () -> serviceResponseDTO.setDescription("Update Template Not Found")
                                            );
                                } else {
                                    CampaignTemplateEntity campaignTemplate = campaignTemplateEntitySetValues(new CampaignTemplateEntity(), ruleRootModel);
                                    log.info("LOG:: CampaignTemplateServiceImpl saveTemplate Save");
                                    campaignTemplate.setCreatedDate(new Date().toString());
                                    campaignTemplate.setCreator(ruleRootModel.getCreator());
                                    // Save
                                    serviceResponseDTO.setData(campaignTemplateRepository.save(campaignTemplate));
                                    serviceResponseDTO.setDescription("Save Template Success");
                                }
                                serviceResponseDTO.setMessage(STATUS_SUCCESS);
                                serviceResponseDTO.setCode(STATUS_2000);
                            },
                            () -> serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND)
                    );
        } catch (Exception exception) {
            log.info("LOG :: CampaignTemplateServiceImpl saveTemplate() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl saveTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
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
            CampaignTemplateCustomObject campaignTemplateCustomObject = new CampaignTemplateCustomObject();
            CompletableFuture.runAsync(() -> campaignTemplateCustomObject.setTotal(campaignTemplateRepository.count()));
            Page<CampaignTemplateEntity> campaignTemplateEntities = campaignTemplateRepository.findAll(PageRequest.of(page, size));
            campaignTemplateCustomObject.setCampaignTemplateDTOS(campaignTemplateEntities);
            serviceResponseDTO.setDescription("Get AllCamp Template With Pagination Success");
            serviceResponseDTO.setData(campaignTemplateCustomObject);
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: CampaignTemplateServiceImpl getAllCampTemplatePagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl getAllCampTemplatePagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
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
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: CampaignTemplateServiceImpl deleteTemplate() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("CampaignTemplateServiceImpl deleteTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(OK);
        return serviceResponseDTO;
    }

    CampaignTemplateEntity campaignTemplateEntitySetValues(CampaignTemplateEntity campaignTemplateEntity, CampaignTemplateDTO ruleRootModel) {
        campaignTemplateEntity.setCampaignName(ruleRootModel.getCampaignName());
        campaignTemplateEntity.setOrganization(ruleRootModel.getOrganizationUuid());
        campaignTemplateEntity.setChannels(ruleRootModel.getChannels());
        campaignTemplateEntity.setChannelIds(NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getChannelIds()) ? ruleRootModel.getChannelIds() : campaignTemplateEntity.getChannelIds());
        campaignTemplateEntity.setChannels(NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getChannels()) ? ruleRootModel.getChannels() : campaignTemplateEntity.getChannels());
        campaignTemplateEntity.setTags(NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getTags()) ? ruleRootModel.getTags() : campaignTemplateEntity.getTags());
        campaignTemplateEntity.setUpdater(NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getUpdater()) ? ruleRootModel.getUpdater() : campaignTemplateEntity.getUpdater());
        campaignTemplateEntity.setStartDateTime(NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getStartDateTime()) ? ruleRootModel.getStartDateTime() : campaignTemplateEntity.getStartDateTime());
        campaignTemplateEntity.setCampaignDescription(NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getCampaignDescription()) ? ruleRootModel.getCampaignDescription() : campaignTemplateEntity.getCampaignDescription());
        campaignTemplateEntity.setEndDateTime(NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRootModel.getEndDateTime()) ? ruleRootModel.getEndDateTime() : campaignTemplateEntity.getEndDateTime());
        return campaignTemplateEntity;
    }

}
