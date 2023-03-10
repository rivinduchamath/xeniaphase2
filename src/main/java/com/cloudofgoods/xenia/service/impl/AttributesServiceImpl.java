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
import com.cloudofgoods.xenia.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
                                        attributeEntity.setAttributesId(new AttributesId(attributesDTO.getOrganizationUuid(), attributesDTO.getAttributeName()));
                                        attributeEntity.setDisplayName(NotEmptyOrNullValidator.isNotNullOrEmpty(attributesDTO.getDisplayName())
                                                ? attributesDTO.getDisplayName() : attributeEntity.getDisplayName());
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
                            AttributeEntity attributeEntity = new AttributeEntity();
                            attributeEntity.setAttributeUuid(Utils.timeUuidGenerate());
                            attributeEntity.setAttributesId(new AttributesId(attributesDTO.getOrganizationUuid(), attributesDTO.getAttributeName()));
                            attributeEntity.setDisplayName(attributesDTO.getDisplayName());
                            attributeEntity.setType(attributesDTO.getType());
                            attributeEntity.setStatus(attributesDTO.isStatus());
                            attributeEntity.setValues(NotEmptyOrNullValidator.isNullOrEmptyList(attributesDTO.getValues())
                                    ? attributesDTO.getValues() : attributeEntity.getValues());
                            attributeEntity.setTableName(attributesDTO.getTableName());
                            serviceResponseDTO.setData(responseAttribute(attributeRepository.save(attributeEntity)));
                            serviceResponseDTO.setDescription("Save Attribute Success");
                        }
                    },
                    () -> {
                        serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND);
                    }
            );
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl saveAttribute() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AttributesServiceImpl saveAttribute() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceGetResponseDTO getAttribute(GetRequestAttributeDTO requestAttributeDTO) {
        log.info("LOG:: AttributesServiceImpl getAttribute Service Layer");
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            serviceGetResponseDTO.setCount(attributeRepository.countByStatusEqualsAndAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrAttributesIdOrganizationUuidEqualsAndTypeInAndStatusEquals(true,requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(),requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getType(), true));
            List<AttributeEntity> attributeEntities = requestAttributeDTO.isPagination()
                    ? attributeRepository.findAllByStatusEqualsAndAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrStatusEqualsAndAttributesIdOrganizationUuidEqualsAndTypeIn(true,
                    requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(), true, requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getType(),
                    PageRequest.of(requestAttributeDTO.getPage(), requestAttributeDTO.getSize()))
                    : attributeRepository.findAllByStatusEqualsAndAttributesId_OrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrStatusEqualsAndAttributesIdOrganizationUuidEqualsAndTypeIn(true,
                    requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(),true, requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getType());
            serviceGetResponseDTO.setData(attributeEntities.stream().map(this::responseAttribute).collect(Collectors.toList()));
            serviceGetResponseDTO.setDescription("Get Attribute Success");
            serviceGetResponseDTO.setMessage(STATUS_SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl getAttribute() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("AttributesServiceImpl getAttribute() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(STATUS_FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

    @Override
    @Transactional(readOnly = true)
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
            serviceGetResponseDTO.setMessage(STATUS_SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl getSingleAttribute() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("AttributesServiceImpl getSingleAttribute() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(STATUS_FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

    @Override
    @Transactional
    public ServiceResponseDTO activeInactiveAttribute(String attributeUuid, String organizationUuid, boolean status) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AttributesServiceImpl deleteAttribute");
        try {
            attributeRepository.findByAttributesId_OrganizationUuidEqualsAndAttributeUuidEquals(organizationUuid, attributeUuid)
                                .ifPresentOrElse(attributeEntity -> {
                                    attributeEntity.setStatus(status);
                                    serviceResponseDTO.setData(attributeRepository.save(attributeEntity));
                                    serviceResponseDTO.setMessage(STATUS_SUCCESS);
                                    serviceResponseDTO.setDescription("Active Or InActive Attribute,  Success");
                                }, () -> {
                                    serviceResponseDTO.setMessage(STATUS_FAIL);
                                    serviceResponseDTO.setDescription("Active Or InActive Attribute, attribute or Organization may Not Found");
                                });
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl deleteAttribute() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("Active Or InActive Attribute, exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
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
}
