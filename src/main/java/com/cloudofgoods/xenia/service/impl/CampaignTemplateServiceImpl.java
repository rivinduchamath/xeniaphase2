package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.CampaignTemplateDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.models.CampaignTemplateCustomObject;
import com.cloudofgoods.xenia.entity.xenia.CampaignTemplateEntity;
import com.cloudofgoods.xenia.repository.CampaignTemplateRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.CampaignTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class CampaignTemplateServiceImpl implements CampaignTemplateService {
    @Autowired
    private CampaignTemplateRepository campaignTemplateRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Override
    public ServiceResponseDTO saveOrUpdateTemplate(CampaignTemplateDTO ruleRootModel) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        log.info ("LOG:: CampaignTemplateServiceImpl saveTemplate");
        try {
            CampaignTemplateEntity campaignTemplateEntity = new CampaignTemplateEntity ();
                Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(
                        ruleRootModel.getOrganizationUuid());
                if (organization.isPresent() &&  NotEmptyOrNullValidator.isNullOrEmpty(ruleRootModel.getId())) {
                    log.info("LOG:: CampaignTemplateServiceImpl saveTemplate Update");
                    campaignTemplateEntity.setId(ruleRootModel.getId());
                    campaignTemplateEntity.setCreatedDate (ruleRootModel.getCreatedDate ());
                    campaignTemplateEntity=
                            campaignTemplateEntitySetValues(campaignTemplateEntity, ruleRootModel);
                    campaignTemplateEntity = campaignTemplateRepository.save(campaignTemplateEntity);// Update
                    serviceResponseDTO.setData(campaignTemplateEntity);
                    serviceResponseDTO.setDescription("Update Template Success");
                    serviceResponseDTO.setMessage("Success");
                    serviceResponseDTO.setCode("2000");
                    serviceResponseDTO.setHttpStatus("OK");
                } else {
                    log.info("LOG:: CampaignTemplateServiceImpl saveTemplate Save");
                    campaignTemplateEntity = campaignTemplateEntitySetValues(campaignTemplateEntity, ruleRootModel);
                    campaignTemplateEntity.setCreatedDate (new Date().toString());
                    campaignTemplateEntity.setCreator (ruleRootModel.getCreator ());
                    campaignTemplateEntity=  campaignTemplateRepository.save(campaignTemplateEntity);// Save
                    serviceResponseDTO.setData(campaignTemplateEntity);
                    serviceResponseDTO.setDescription("Save Template Success");
                    serviceResponseDTO.setMessage("Success");
                    serviceResponseDTO.setCode("2000");
                    serviceResponseDTO.setHttpStatus("OK");
                }
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: CampaignTemplateServiceImpl saveTemplate() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setDescription ("CampaignTemplateServiceImpl saveTemplate() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }
    @Override
    public ServiceResponseDTO getAllCampTemplatePagination(int page, int size) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        log.info ("LOG:: CampaignTemplateServiceImpl getAllCampTemplatePagination()");
        try {
            Page <CampaignTemplateEntity> campaignTemplateEntities = campaignTemplateRepository.findAll (PageRequest.of (page, size));
            long count = campaignTemplateRepository.count ();

            CampaignTemplateCustomObject campaignTemplateCustomObject = new CampaignTemplateCustomObject ();
            campaignTemplateCustomObject.setCampaignTemplateDTOS (campaignTemplateEntities);
            campaignTemplateCustomObject.setTotal (count);
            serviceResponseDTO.setDescription ("Get AllCamp Template With Pagination Success");
            serviceResponseDTO.setData (campaignTemplateCustomObject);
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: CampaignTemplateServiceImpl getAllCampTemplatePagination() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setDescription ("CampaignTemplateServiceImpl getAllCampTemplatePagination() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO deleteTemplate(String templateId) {
        log.info ("LOG :: CampaignTemplateServiceImpl deleteTemplate()");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        try {
            campaignTemplateRepository.deleteById (templateId);
            serviceResponseDTO.setMessage ("Delete Template Success");
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: CampaignTemplateServiceImpl deleteTemplate() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setMessage ("CampaignTemplateServiceImpl deleteTemplate() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }

    CampaignTemplateEntity campaignTemplateEntitySetValues(CampaignTemplateEntity campaignTemplateEntity, CampaignTemplateDTO ruleRootModel) {
        if(NotEmptyOrNullValidator.isNullOrEmpty(ruleRootModel.getCampaignDescription ())) campaignTemplateEntity.setCampaignDescription (ruleRootModel.getCampaignDescription ());
        campaignTemplateEntity.setCampaignName (ruleRootModel.getCampaignName ());
        campaignTemplateEntity.setOrganization (ruleRootModel.getOrganizationUuid());
        if(NotEmptyOrNullValidator.isNullOrEmpty(ruleRootModel.getUpdater ())) campaignTemplateEntity.setUpdater (ruleRootModel.getUpdater ());
        if(NotEmptyOrNullValidator.isNullOrEmpty(ruleRootModel.getStartDateTime ())) campaignTemplateEntity.setStartDateTime (ruleRootModel.getStartDateTime ());
        if(NotEmptyOrNullValidator.isNullOrEmpty(ruleRootModel.getEndDateTime ())) campaignTemplateEntity.setEndDateTime (ruleRootModel.getEndDateTime ());
        campaignTemplateEntity.setChannels(ruleRootModel.getChannels());
        if(NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getTags ())) campaignTemplateEntity.setTags(ruleRootModel.getTags());
        if(NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getChannels ())) campaignTemplateEntity.setChannels(ruleRootModel.getChannels());
        if(NotEmptyOrNullValidator.isNullOrEmptyList(ruleRootModel.getChannelIds ())) campaignTemplateEntity.setChannelIds(ruleRootModel.getChannelIds());
        return campaignTemplateEntity;
    }

}
