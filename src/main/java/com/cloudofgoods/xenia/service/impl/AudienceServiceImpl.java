package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.AudienceDTO;
import com.cloudofgoods.xenia.dto.request.AudienceGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AudienceRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AudienceEntity;
import com.cloudofgoods.xenia.models.responces.AudienceResponseObject;
import com.cloudofgoods.xenia.repository.AudienceRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AudienceService;
import com.cloudofgoods.xenia.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.cloudofgoods.xenia.util.Utils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudienceServiceImpl implements AudienceService {
    private final AudienceRepository audienceRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public ServiceResponseDTO saveAudience(AudienceDTO audienceDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AudienceServiceImpl saveAudience Service Layer");
        try {
            organizationRepository.findByUuidEquals(audienceDTO.getOrganizationUuid()).ifPresentOrElse(org -> {
                log.info("LOG:: AudienceServiceImpl saveAudience {}", NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceUuid()) ? "Update" : "Save");
                boolean notNullOrEmpty = NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceUuid());
                if (notNullOrEmpty) {
                    Optional<AudienceEntity> audienceEntity = audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(
                            audienceDTO.getAudienceUuid(), audienceDTO.getOrganizationUuid());
                    audienceEntity.ifPresentOrElse(entity -> {
                        serviceResponseDTO.setData(audienceRepository.save(setAudienceValues(audienceDTO, entity)));
                        serviceResponseDTO.setDescription("Update Audience Success");
                    }, () -> {
                        serviceResponseDTO.setDescription("Update Audience Fail Audience Not Found");
                    });
                } else {
                    AudienceEntity newAudienceEntity = new AudienceEntity();
                    newAudienceEntity.setAudienceUuid(Utils.timeUuidGenerate());
                    newAudienceEntity.setStatus((true));
                    serviceResponseDTO.setData(audienceRepository.save(setAudienceValues(audienceDTO, newAudienceEntity)));
                    serviceResponseDTO.setDescription("Save Audience Success");
                }
            }, () -> serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND));
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl saveAudience() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AudienceServiceImpl saveAudience() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    AudienceEntity setAudienceValues(AudienceDTO audienceDTO, AudienceEntity newAudience) {
        newAudience.setAudienceName(audienceDTO.getAudienceName());
        newAudience.setAudienceDescription(NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceDescription()) ? audienceDTO.getAudienceDescription() : newAudience.getAudienceDescription());
        newAudience.setAudienceRuleString(NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceRuleString()) ? audienceDTO.getAudienceRuleString() : newAudience.getAudienceRuleString());
        newAudience.setOrganizationUuid(audienceDTO.getOrganizationUuid());
        newAudience.setAudienceObject(NotEmptyOrNullValidator.isNullObject(audienceDTO.getAudienceObject()) ? audienceDTO.getAudienceObject() : newAudience.getAudienceObject());
        return newAudience;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponseDTO getAudienceById(AudienceGetSingleDTO audienceGetSingleDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG :: AudienceServiceImpl getAudienceById() audienceUuid : " + audienceGetSingleDTO.getAudienceUuId());
        try {
            audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceGetSingleDTO.getAudienceUuId(), audienceGetSingleDTO.getOrganizationUuid()).ifPresentOrElse(audienceEntity -> {
                serviceResponseDTO.setData(audienceEntity);
                serviceResponseDTO.setDescription("AudienceService Get Audience success");
            }, () -> serviceResponseDTO.setDescription("AudienceService Audience Not Found Under this Organization"));
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl getAudienceById() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AudienceServiceImpl getAudienceById() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponseDTO getAudienceWithPagination(AudienceRequestDTO audienceRequestDTO) {
        log.info("LOG:: AudienceServiceImpl getAudienceWithPagination()");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            AudienceResponseObject audienceResponseObject = new AudienceResponseObject();
            CompletableFuture.runAsync(() ->  audienceResponseObject.setTotal(audienceRepository.countAllByOrganizationUuidEqualsAndStatusEquals(audienceRequestDTO.getOrganizationId() ,true)));
            audienceResponseObject.setAudienceEntities(audienceRequestDTO.isPagination()
                    ? audienceRepository.findAllByOrganizationUuidEqualsAndAudienceNameStartingWithAndStatusEquals(audienceRequestDTO.getOrganizationId(), audienceRequestDTO.getAudienceName(), true,PageRequest.of(audienceRequestDTO.getPage(), audienceRequestDTO.getSize()))
                    : audienceRepository.findByOrganizationUuidEqualsAndAudienceNameStartingWithAndStatusEquals(audienceRequestDTO.getOrganizationId(), audienceRequestDTO.getAudienceName(),true));
            serviceResponseDTO.setData(audienceResponseObject);
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination Success");
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl getAudienceWithPagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO activeInactiveAudience(String audienceUuid, String organizationUuid, boolean status) {
        log.info("LOG:: AudienceServiceImpl activeInactiveAudience");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
                audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceUuid, organizationUuid).ifPresentOrElse(entity -> {
                    entity.setStatus(status);
                    serviceResponseDTO.setData(audienceRepository.save(entity));
                    serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() Success");
                    serviceResponseDTO.setMessage(STATUS_SUCCESS);
                }, () -> {
                    serviceResponseDTO.setDescription("Attribute Or Organization may Not Found");
                    serviceResponseDTO.setMessage(STATUS_FAIL);
                });
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl activeInactiveAudience() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

}
