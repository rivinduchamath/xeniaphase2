package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.AttributeTableDTO;
import com.cloudofgoods.xenia.dto.composite.AttributeTableId;
import com.cloudofgoods.xenia.dto.request.AttributeTableGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableRequestDTO;
import com.cloudofgoods.xenia.dto.response.AttributeTableResponseDTO;
import com.cloudofgoods.xenia.dto.response.AttributeTableSaveUpdateResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import com.cloudofgoods.xenia.repository.AttributeTableRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AttributesTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cloudofgoods.xenia.util.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttributesTableServiceImpl implements AttributesTableService {
    private final AttributeTableRepository attributeTableRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public ServiceResponseDTO saveOrUpdateAttributeTable(AttributeTableDTO attributeTableDTO) {
        log.info("LOG:: AttributesTableServiceImpl saveOrUpdateAttributeTable()");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            organizationRepository.findOrganizationEntityByUuidEquals(attributeTableDTO.getOrganizationUuid()).ifPresentOrElse(organizationEntity -> {
                if (NotEmptyOrNullValidator.isNotNullOrEmpty(attributeTableDTO.getId())) {
                    attributeTableRepository.findAllByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(attributeTableDTO.getId(), attributeTableDTO.getOrganizationUuid())
                            .ifPresentOrElse(attributeTableEntity -> {
                                serviceResponseDTO.setData(setSaveOrUpdateResponse(attributeTableRepository.save(attributeTableEntityCreate(attributeTableEntity, attributeTableDTO))));
                                serviceResponseDTO.setDescription("Attributes Table Service update success");
                            }, () -> serviceResponseDTO.setDescription("Attributes Table Service update Fail attribute Not Found"));
                } else {
                    AttributeTableEntity attributeTableEntity = new AttributeTableEntity();
                    serviceResponseDTO.setDescription("Attributes Table Service save success");
                    serviceResponseDTO.setData(setSaveOrUpdateResponse(attributeTableRepository.save(attributeTableEntityCreate(attributeTableEntity, attributeTableDTO))));
                }
                serviceResponseDTO.setMessage(STATUS_SUCCESS);
                serviceResponseDTO.setCode(STATUS_2000);
            }, () -> serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND));
        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveAttributeTable() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceGetResponseDTO getAttributesTables(AttributeTableRequestDTO attributeTableDTO) {
        ServiceGetResponseDTO serviceResponseDTO = new ServiceGetResponseDTO();
        log.info("LOG:: AttributesTableServiceImpl getAttributesTables()");
        try {
            AttributeTableResponseDTO attributeTableResponseDTO = new AttributeTableResponseDTO();
            serviceResponseDTO.setCount(attributeTableRepository.countByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEqualsAndStatusEquals(attributeTableDTO.getName().toLowerCase(), attributeTableDTO.getOrganizationUuid(), true));
            List<AttributeTableEntity> stream = attributeTableDTO.isPagination() ?
                    attributeTableRepository.findAllByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameStartingWithAndStatusEquals(attributeTableDTO.getOrganizationUuid(), attributeTableDTO.getName().toLowerCase(), true, PageRequest.of(attributeTableDTO.getPage(), attributeTableDTO.getSize())) :
                    attributeTableRepository.findAllByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableIdOrganizationUuidEqualsAndStatusEquals(attributeTableDTO.getName().toLowerCase(), attributeTableDTO.getOrganizationUuid(), true);
            serviceResponseDTO.setMessage("AttributesTableServiceImpl Get Attributes " + (attributeTableDTO.isPagination() ? "With Pagination" : "Without Pagination") + " Success");
            attributeTableResponseDTO.setAttributeTableEntities(stream);
            serviceResponseDTO.setData(attributeTableResponseDTO);
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesTableServiceImpl getAttributes() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("AttributesTableServiceImpl getAttributes() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    @Transactional
    public ServiceResponseDTO activeInactiveAttributeTable(String attributeTableName, String organizationUuid, boolean status) {
        log.info("LOG:: AttributesTableServiceImpl activeInactiveAttributeTable");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            attributeTableRepository.findByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(attributeTableName.toLowerCase(), organizationUuid).ifPresentOrElse(attributeTableEntity -> {
                attributeTableEntity.setStatus(status);
                serviceResponseDTO.setData(attributeTableRepository.save(attributeTableEntity));
                serviceResponseDTO.setDescription("Active/Inactive Attribute Table Success");
            }, () -> serviceResponseDTO.setDescription("Cannot Find Attribute Table Under this Organization"));
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesTableServiceImpl activeInactiveAttributeTable() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AttributesTableServiceImpl activeInactiveAttributeTable() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getSingleAttributeTable(AttributeTableGetSingleDTO attributeRequestDTO) {
        log.info("LOG :: AttributesTableServiceImpl getSingleAttributeTable() Service Layer");
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            attributeTableRepository.findByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameEquals(attributeRequestDTO.getOrganizationUuid(), attributeRequestDTO.getAttributeTableName().toLowerCase()).ifPresentOrElse(attributeTableEntity -> {
                serviceGetResponseDTO.setData(attributeTableEntity);
                serviceGetResponseDTO.setMessage(STATUS_SUCCESS);
                serviceGetResponseDTO.setDescription("Get Attribute Table Success");
            }, () -> serviceGetResponseDTO.setDescription("Cannot Find Attribute Table Under Organization"));
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl getSingleAttributeTable() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("AttributesServiceImpl getSingleAttributeTable() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(STATUS_FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

    private AttributeTableEntity attributeTableEntityCreate(AttributeTableEntity attributeTableEntity, AttributeTableDTO attributeTableDTO) {
        attributeTableEntity.setAttributeTableId(new AttributeTableId(attributeTableDTO.getTableName().toLowerCase(), attributeTableDTO.getOrganizationUuid()));
        attributeTableEntity.setDisplayName(NotEmptyOrNullValidator.isNotNullOrEmpty(attributeTableDTO.getDisplayName()) ? attributeTableDTO.getDisplayName() :
                attributeTableEntity.getDisplayName());
        attributeTableEntity.setStatus(attributeTableDTO.isStatus());
        return attributeTableEntity;
    }

    private AttributeTableSaveUpdateResponseDTO setSaveOrUpdateResponse(AttributeTableEntity attributeTableEntity) {
        AttributeTableSaveUpdateResponseDTO attributeTableSaveUpdateResponseDTO = new AttributeTableSaveUpdateResponseDTO();
        attributeTableSaveUpdateResponseDTO.setTableName(attributeTableEntity.getAttributeTableId().getAttributeTableName());
        attributeTableSaveUpdateResponseDTO.setDisplayName(attributeTableEntity.getDisplayName());
        attributeTableSaveUpdateResponseDTO.setActive(attributeTableEntity.isStatus());
        return attributeTableSaveUpdateResponseDTO;
    }

}
