package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.AudienceDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AudienceEntity;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.models.responces.AudienceResponseObject;
import com.cloudofgoods.xenia.repository.AudienceRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AudienceService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudienceServiceImpl implements AudienceService {
    private final AudienceRepository audienceRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveAudience(AudienceDTO audienceDTO) {
        log.info("LOG:: AudienceServiceImpl saveAudience Service Layer");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
                Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(audienceDTO.getOrganizationUuid());
                if (organization.isPresent() && NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getAudienceUuid())) {
                    AudienceEntity audienceEntity = audienceRepository.findByAudienceUuid(audienceDTO.getAudienceUuid());
                    log.info("LOG:: AudienceServiceImpl saveAudience Update");
                    audienceEntity = setAudienceValues(audienceDTO, audienceEntity);
                    AudienceEntity save = audienceRepository.save(audienceEntity);// Update
                    serviceResponseDTO.setData(save);
                    serviceResponseDTO.setDescription("Update Audience Success");
                } else {
                    log.info("LOG:: AudienceServiceImpl saveAudience Save");
                    AudienceEntity audienceEntity = new AudienceEntity();
                    NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                    UUID firstUUID = timeBasedGenerator.generate();
                    audienceEntity.setAudienceUuid(firstUUID.toString());
                    audienceEntity = setAudienceValues(audienceDTO, audienceEntity);
                    AudienceEntity save = audienceRepository.save(audienceEntity);
                    serviceResponseDTO.setData(save);
                    serviceResponseDTO.setDescription("Save Audience Success");
                }
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");

        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl saveAudience() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AudienceServiceImpl saveAudience() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }

    AudienceEntity setAudienceValues(AudienceDTO audienceDTO, AudienceEntity newAudience) {
        if (NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getAudienceName())) newAudience.setAudienceName(audienceDTO.getAudienceName());
        if (NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getAudienceDescription())) newAudience.setAudienceDescription(audienceDTO.getAudienceDescription());
        if (NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getAudienceRuleString())) newAudience.setAudienceRuleString(audienceDTO.getAudienceRuleString());
        if (NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getOrganizationUuid())) newAudience.setOrganizationUuid(audienceDTO.getOrganizationUuid());
        if (NotEmptyOrNullValidator.isNullObject(audienceDTO.getAudienceObject())) newAudience.setAudienceObject(audienceDTO.getAudienceObject());
        return newAudience;
    }

    @Override
    public ServiceResponseDTO getAudienceById(String audienceUuid) {

        log.info("LOG :: AudienceServiceImpl getAudienceById() audienceUuid" + audienceUuid);
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            AudienceEntity byAudienceUuid = audienceRepository.findByAudienceUuid(audienceUuid);
            serviceResponseDTO.setData(byAudienceUuid);
            serviceResponseDTO.setDescription("AudienceServiceImpl getAudienceById() success");
            serviceResponseDTO.setMessage("success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl getAudienceById() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AudienceServiceImpl getAudienceById() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
        }
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getAudienceWithPagination(String organizationId, int page, int size) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AudienceServiceImpl getAudienceWithPagination()");
        try {
            AudienceResponseObject audienceResponseObject = new AudienceResponseObject();
            List<AudienceEntity> stream = audienceRepository.findAllByOrganizationUuidEquals(organizationId, PageRequest.of(page, size));
            long totalCount = audienceRepository.countAllByOrganizationUuidEquals(organizationId);
            audienceResponseObject.setAudienceEntities(stream);
            audienceResponseObject.setTotal(totalCount);
            serviceResponseDTO.setData(audienceResponseObject);
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl getAudienceWithPagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }
}
