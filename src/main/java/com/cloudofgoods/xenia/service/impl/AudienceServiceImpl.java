package com.cloudofgoods.xenia.service.impl;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AudienceServiceImpl implements AudienceService {
    @Autowired
    private AudienceRepository audienceRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveAudience(AudienceDTO audienceDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            if (!audienceDTO.getOrganizationUuid().equals("")) {
                Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(audienceDTO.getOrganizationUuid());
                if (organization.isPresent() && audienceDTO.getAudienceUuid() != null) {
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
            } else {
                serviceResponseDTO.setDescription("OrganizationUuid Cannot Be Null");
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
        if (audienceDTO.getAudienceName() != null) newAudience.setAudienceName(audienceDTO.getAudienceName());
        if (audienceDTO.getAudienceDescription() != null)
            newAudience.setAudienceDescription(audienceDTO.getAudienceDescription());
        if (audienceDTO.getAudienceRuleString() != null)
            newAudience.setAudienceRuleString(audienceDTO.getAudienceRuleString());
        if (audienceDTO.getOrganizationUuid() != null)
            newAudience.setOrganizationUuid(audienceDTO.getOrganizationUuid());
        if (audienceDTO.getAudienceObject() != null)
            newAudience.setAudienceObject(audienceDTO.getAudienceObject());
        if (audienceDTO.getAudienceObject() != null)
            newAudience.setAudienceObject(audienceDTO.getAudienceObject());
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
