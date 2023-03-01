package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.request.AttributeGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.response.AttributeResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AttributeEntity;
import com.cloudofgoods.xenia.models.composite.AttributesId;
import com.cloudofgoods.xenia.repository.AttributeRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AttributesService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.cloudofgoods.xenia.util.Utils.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class AttributesServiceImpl implements AttributesService {

    private final AttributeRepository attributeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveAttribute(AttributeRequestDTO attributesDTO) {
        log.info("LOG:: AttributesServiceImpl saveAttribute Service Layer");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            organizationRepository.findByUuidEquals(attributesDTO.getOrganizationUuid()).ifPresentOrElse(
                    organizationEntity -> {
                        if (NotEmptyOrNullValidator.isNotNullOrEmpty(attributesDTO.getAttributeUuid())) { // Update
                            log.info("LOG:: AttributesServiceImpl saveAttribute Service Layer Update");
                            attributeRepository.findByAttributeUuidEquals(attributesDTO.getAttributeUuid()).ifPresentOrElse(
                                    attributeEntity -> {
                                        attributeEntity.setAttributeUuid(attributesDTO.getAttributeUuid());
                                        attributeEntity.setAttributesId(new AttributesId(attributesDTO.getOrganizationUuid(), attributesDTO.getAttributeName()));
                                        attributeEntity.setDisplayName(NotEmptyOrNullValidator.isNotNullOrEmpty(attributesDTO.getAttributeName())
                                                ? attributesDTO.getAttributeName() : attributeEntity.getDisplayName());
                                        attributeEntity.setType(attributesDTO.getType());
                                        attributeEntity.setStatus(attributesDTO.isStatus());
                                        attributeEntity.setValues(NotEmptyOrNullValidator.isNullOrEmptyList(attributesDTO.getValues())
                                                ? attributesDTO.getValues() : attributeEntity.getValues());
                                        attributeEntity.setTableName(attributesDTO.getTableName());
                                        serviceResponseDTO.setData(responseAttribute(attributeRepository.save(attributeEntity)));
                                        serviceResponseDTO.setDescription("Update Attribute Success");
                                    },
                                    () -> {
                                        serviceResponseDTO.setDescription("Update Attribute Fail Attribute Not Found");
                                    }
                            );
                        } else {  // Save
                            log.info("LOG:: AttributesServiceImpl saveAttribute Service Layer Save");
                            NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                            AttributeEntity attributeEntity = new AttributeEntity();
                            attributeEntity.setAttributeUuid(timeBasedGenerator.generate() + "");
                            attributeEntity.setAttributesId((NotEmptyOrNullValidator.isNotNullOrEmpty(attributesDTO.getOrganizationUuid()) && NotEmptyOrNullValidator.isNotNullOrEmpty(attributesDTO.getAttributeName()))
                                    ? new AttributesId(attributesDTO.getOrganizationUuid(), attributesDTO.getAttributeName()) : null);
                            attributeEntity.setDisplayName(attributesDTO.getDisplayName());
                            attributeEntity.setType(attributesDTO.getType());
                            attributeEntity.setStatus(attributesDTO.isStatus());
                            attributeEntity.setValues(attributesDTO.getValues());
                            attributeEntity.setTableName(attributesDTO.getTableName());
                            serviceResponseDTO.setData(responseAttribute(attributeRepository.save(attributeEntity)));
                            serviceResponseDTO.setDescription("Save Attribute Success");
                        }
                    },
                    () -> {
                        serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND);
                    }
            );
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl saveAttribute() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AttributesServiceImpl saveAttribute() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    private AttributeResponseDTO responseAttribute(AttributeEntity attributeEntity) {
        AttributeResponseDTO attributeResponseDTO = new AttributeResponseDTO();
        attributeResponseDTO.setAttributeName(attributeEntity.getAttributesId().getAttributeName());
        attributeResponseDTO.setAttributeUuid(attributeEntity.getAttributeUuid());
        attributeResponseDTO.setDisplayName(attributeEntity.getDisplayName());
        attributeResponseDTO.setType(attributeEntity.getType());
        attributeResponseDTO.setStatus(attributeEntity.isStatus());
        attributeResponseDTO.setValues(attributeEntity.getValues());
        attributeResponseDTO.setTableName(attributeEntity.getTableName());
        return attributeResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getAttribute(GetRequestAttributeDTO requestAttributeDTO) {
        log.info("LOG:: AttributesServiceImpl getAttribute Service Layer");
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            List<AttributeEntity> attributeEntities = requestAttributeDTO.isPagination()
                    ? attributeRepository.findAllByAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrAttributesIdOrganizationUuidEqualsAndTypeIn(
                    requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(), requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getType(),
                    PageRequest.of(requestAttributeDTO.getPage(), requestAttributeDTO.getSize()))
                    : attributeRepository.findAllByAttributesId_OrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrAttributesIdOrganizationUuidEqualsAndTypeIn(
                    requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(), requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getType());
            List<AttributeResponseDTO> attributeResponseDTOS = attributeEntities.stream().map(this::responseAttribute).collect(Collectors.toList());
            serviceGetResponseDTO.setCount(attributeRepository.countByAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrTypeIn(requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(), requestAttributeDTO.getType()));
            serviceGetResponseDTO.setData(attributeResponseDTOS);
            serviceGetResponseDTO.setDescription("Get Attribute Success");
            serviceGetResponseDTO.setMessage(SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl getAttribute() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("AttributesServiceImpl getAttribute() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getSingleAttribute(AttributeGetSingleDTO attributeRequestDTO) {
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            attributeRepository.findByAttributesId_OrganizationUuidEqualsAndAttributeUuidEquals(attributeRequestDTO.getOrganizationUuid(), attributeRequestDTO.getAttributeUuid())
                    .ifPresent(attributeEntity -> {
                        serviceGetResponseDTO.setData(attributeEntity);
                        serviceGetResponseDTO.setDescription("Get Attribute Success");
                    });
            if (!NotEmptyOrNullValidator.isNotNullOrEmpty(serviceGetResponseDTO.getDescription())) {
                serviceGetResponseDTO.setDescription("Cannot Find Data");
            }
            serviceGetResponseDTO.setMessage(SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl getSingleAttribute() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("AttributesServiceImpl getSingleAttribute() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

    @Override
    public ServiceResponseDTO activeInactiveAttribute(String attributeUuid, String organizationUuid, boolean status) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AttributesServiceImpl deleteAttribute");
        try {
            organizationRepository.findOrganizationEntityByUuidEquals(organizationUuid)
                    .ifPresent(organizationEntity -> {
                        attributeRepository.findByAttributeUuidEquals(attributeUuid)
                                .ifPresentOrElse(attributeEntity -> {
                                    attributeEntity.setStatus(status);
                                    serviceResponseDTO.setData(attributeRepository.save(attributeEntity));
                                    serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() Success");
                                }, () -> {
                                    serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() attribute Not Found");
                                });
                    });
            if (!NotEmptyOrNullValidator.isNotNullOrEmpty(serviceResponseDTO.getDescription())) {
                serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() Organization Not Found");
            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl deleteAttribute() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }
}
