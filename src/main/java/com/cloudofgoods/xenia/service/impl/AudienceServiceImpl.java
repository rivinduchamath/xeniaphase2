package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.AudienceDTO;
import com.cloudofgoods.xenia.dto.request.AudienceGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AudienceRequestDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cloudofgoods.xenia.util.Utils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudienceServiceImpl implements AudienceService {
    private final AudienceRepository audienceRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceResponseDTO serviceResponseDTO;

    @Override
    public ServiceResponseDTO saveAudience(AudienceDTO audienceDTO) {
        log.info("LOG:: AudienceServiceImpl saveAudience Service Layer");
        try {
            Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(audienceDTO.getOrganizationUuid());
            if (organization.isPresent()) {
                if (NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getAudienceUuid())) {
                    log.info("LOG:: AudienceServiceImpl saveAudience Update");// Update
                    Optional<AudienceEntity> audienceEntity = audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceDTO.getAudienceUuid(), audienceDTO.getOrganizationUuid());
                    if (audienceEntity.isPresent()) {
                        serviceResponseDTO.setData(audienceRepository.save(setAudienceValues(audienceDTO, audienceEntity.get())));
                        serviceResponseDTO.setDescription("Update Audience Success");
                    } else {
                        serviceResponseDTO.setDescription("Update Audience Fail Audience not Fount Under this Organization");
                    }
                } else {
                    log.info("LOG:: AudienceServiceImpl saveAudience Save");// Save
                    AudienceEntity audienceEntity = new AudienceEntity();
                    NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                    UUID firstUUID = timeBasedGenerator.generate();
                    audienceEntity.setAudienceUuid(firstUUID.toString());
                    serviceResponseDTO.setData(audienceRepository.save(setAudienceValues(audienceDTO, audienceEntity)));
                    serviceResponseDTO.setDescription("Save Audience Success");
                }
            } else {
                serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND);
            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);

        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl saveAudience() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AudienceServiceImpl saveAudience() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    AudienceEntity setAudienceValues(AudienceDTO audienceDTO, AudienceEntity newAudience) {
        newAudience.setAudienceName(audienceDTO.getAudienceName());
        if (NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getAudienceDescription()))
            newAudience.setAudienceDescription(audienceDTO.getAudienceDescription());
        if (NotEmptyOrNullValidator.isNullOrEmpty(audienceDTO.getAudienceRuleString()))
            newAudience.setAudienceRuleString(audienceDTO.getAudienceRuleString());
        newAudience.setOrganizationUuid(audienceDTO.getOrganizationUuid());
        if (NotEmptyOrNullValidator.isNullObject(audienceDTO.getAudienceObject()))
            newAudience.setAudienceObject(audienceDTO.getAudienceObject());
        return newAudience;
    }

    @Override
    public ServiceResponseDTO getAudienceById(AudienceGetSingleDTO audienceGetSingleDTO) {
        log.info("LOG :: AudienceServiceImpl getAudienceById() audienceUuid : " + audienceGetSingleDTO.getAudienceUuId());
        try {
            Optional<AudienceEntity> byAudienceUuid = audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceGetSingleDTO.getAudienceUuId(), audienceGetSingleDTO.getOrganizationUuid());
            if (byAudienceUuid.isPresent()) {
                serviceResponseDTO.setData(byAudienceUuid.get());
                serviceResponseDTO.setDescription("AudienceService Get Audience success");
            } else {
                serviceResponseDTO.setDescription("AudienceService Audience Not Found Under this Organization");
            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl getAudienceById() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AudienceServiceImpl getAudienceById() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getAudienceWithPagination(AudienceRequestDTO audienceRequestDTO) {
        log.info("LOG:: AudienceServiceImpl getAudienceWithPagination()");
        try {
            AudienceResponseObject audienceResponseObject = new AudienceResponseObject();
            List<AudienceEntity> stream;
            if (audienceRequestDTO.isPagination()) {
                stream = audienceRepository.findAllByOrganizationUuidEqualsAndAudienceNameStartingWith(audienceRequestDTO.getOrganizationId(), audienceRequestDTO.getAudienceName(), PageRequest.of(audienceRequestDTO.getPage(), audienceRequestDTO.getSize()));
            } else {
                stream = audienceRepository.findByOrganizationUuidEqualsAndAudienceNameStartingWith(audienceRequestDTO.getOrganizationId(), audienceRequestDTO.getAudienceName());
            }
            long totalCount = audienceRepository.countAllByOrganizationUuidEquals(audienceRequestDTO.getOrganizationId());
            audienceResponseObject.setAudienceEntities(stream);
            audienceResponseObject.setTotal(totalCount);
            serviceResponseDTO.setData(audienceResponseObject);
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination Success");
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl getAudienceWithPagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO activeInactiveAudience(String audienceUuid, String organizationUuid, boolean status) {
        log.info("LOG:: AudienceServiceImpl activeInactiveAudience");
        try {
            Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationEntityByUuidEquals(organizationUuid);
            if (organizationEntity.isPresent()) {
                Optional<AudienceEntity> audienceEntity = audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceUuid, organizationUuid);
                if (audienceEntity.isPresent()) {
                    audienceEntity.get().setStatus(status);
                    serviceResponseDTO.setData(audienceRepository.save(audienceEntity.get()));
                    serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() Success");
                    serviceResponseDTO.setMessage(SUCCESS);
                } else {
                    serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() attribute Not Found");
                    serviceResponseDTO.setMessage(FAIL);
                }
            } else {
                serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() Organization Not Found");
                serviceResponseDTO.setMessage(FAIL);
            }
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl activeInactiveAudience() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);

        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

}
