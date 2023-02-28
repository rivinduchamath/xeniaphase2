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

    @Override
    public ServiceResponseDTO saveAudience(AudienceDTO audienceDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AudienceServiceImpl saveAudience Service Layer");
        try {
            Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(audienceDTO.getOrganizationUuid());
            organization.ifPresentOrElse(org -> {
                log.info("LOG:: AudienceServiceImpl saveAudience {}", NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceUuid()) ? "Update" : "Save");
                Optional<AudienceEntity> audienceEntity = NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceUuid())
                        ? audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceDTO.getAudienceUuid(), audienceDTO.getOrganizationUuid())
                        : Optional.empty();
                audienceEntity.ifPresentOrElse(entity -> {
                    serviceResponseDTO.setData(audienceRepository.save(setAudienceValues(audienceDTO, entity)));
                    serviceResponseDTO.setDescription("Update Audience Success");
                }, () -> {
                    AudienceEntity newAudienceEntity = new AudienceEntity();
                    NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                    UUID firstUUID = timeBasedGenerator.generate();
                    newAudienceEntity.setAudienceUuid(firstUUID.toString());
                    serviceResponseDTO.setData(audienceRepository.save(setAudienceValues(audienceDTO, newAudienceEntity)));
                    serviceResponseDTO.setDescription("Save Audience Success");
                });
            }, () -> serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND));
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
        newAudience.setAudienceDescription(NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceDescription()) ? audienceDTO.getAudienceDescription() : newAudience.getAudienceDescription());
        newAudience.setAudienceRuleString(NotEmptyOrNullValidator.isNotNullOrEmpty(audienceDTO.getAudienceRuleString()) ? audienceDTO.getAudienceRuleString() : newAudience.getAudienceRuleString());
        newAudience.setOrganizationUuid(audienceDTO.getOrganizationUuid());
        newAudience.setAudienceObject(NotEmptyOrNullValidator.isNullObject(audienceDTO.getAudienceObject()) ? audienceDTO.getAudienceObject() : newAudience.getAudienceObject());
        return newAudience;
    }

    @Override
    public ServiceResponseDTO getAudienceById(AudienceGetSingleDTO audienceGetSingleDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG :: AudienceServiceImpl getAudienceById() audienceUuid : " + audienceGetSingleDTO.getAudienceUuId());
        try {
            audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceGetSingleDTO.getAudienceUuId(), audienceGetSingleDTO.getOrganizationUuid())
                    .ifPresentOrElse(
                            audienceEntity -> {
                                serviceResponseDTO.setData(audienceEntity);
                                serviceResponseDTO.setDescription("AudienceService Get Audience success");
                            },
                            () -> serviceResponseDTO.setDescription("AudienceService Audience Not Found Under this Organization"));
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
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            AudienceResponseObject audienceResponseObject = new AudienceResponseObject();
            List<AudienceEntity> stream = audienceRequestDTO.isPagination() ?
                    audienceRepository.findAllByOrganizationUuidEqualsAndAudienceNameStartingWith(
                            audienceRequestDTO.getOrganizationId(),
                            audienceRequestDTO.getAudienceName(),
                            PageRequest.of(audienceRequestDTO.getPage(), audienceRequestDTO.getSize())
                    ) :
                    audienceRepository.findByOrganizationUuidEqualsAndAudienceNameStartingWith(
                            audienceRequestDTO.getOrganizationId(),
                            audienceRequestDTO.getAudienceName()
                    );
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
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationEntityByUuidEquals(organizationUuid);
            if (organizationEntity.isPresent()) {
                audienceRepository.findByAudienceUuidEqualsAndOrganizationUuidEquals(audienceUuid, organizationUuid).ifPresentOrElse(
                        entity -> {
                            entity.setStatus(status);
                            serviceResponseDTO.setData(audienceRepository.save(entity));
                            serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() Success");
                            serviceResponseDTO.setMessage(SUCCESS);
                        },
                        () -> {
                            serviceResponseDTO.setDescription("AudienceServiceImpl activeInactiveAudience() attribute Not Found");
                            serviceResponseDTO.setMessage(FAIL);
                        }
                );
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
