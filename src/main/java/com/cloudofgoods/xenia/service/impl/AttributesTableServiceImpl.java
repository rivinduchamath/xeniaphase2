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
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.repository.AttributeTableRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AttributesTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.cloudofgoods.xenia.util.Utils.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttributesTableServiceImpl implements AttributesTableService {
    private final AttributeTableRepository attributeTableRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateAttributeTable(AttributeTableDTO attributeTableDTO) {
        log.info("LOG:: AttributesTableServiceImpl saveOrUpdateAttributeTable()");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationEntityByUuidEquals(attributeTableDTO.getOrganizationUuid());
            if (organizationEntity.isPresent()) {
                AttributeTableSaveUpdateResponseDTO attributeTableSaveUpdateResponseDTO = new AttributeTableSaveUpdateResponseDTO();
                if (NotEmptyOrNullValidator.isNotNullOrEmpty(attributeTableDTO.getId())) {
                    Optional<AttributeTableEntity> save = attributeTableRepository.findAllByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(attributeTableDTO.getId(), attributeTableDTO.getOrganizationUuid());
                    if (save.isPresent()) {
                        save.get().setAttributeTableId(new AttributeTableId(attributeTableDTO.getDisplayName(), attributeTableDTO.getOrganizationUuid()));
                        AttributeTableEntity attributeTableEntity = attributeTableRepository.save(save.get());
                        attributeTableSaveUpdateResponseDTO.setTableName(attributeTableEntity.getAttributeTableId().getAttributeTableName());
                        attributeTableSaveUpdateResponseDTO.setDisplayName(attributeTableEntity.getDisplayName());
                        serviceResponseDTO.setData(attributeTableSaveUpdateResponseDTO);
                        serviceResponseDTO.setDescription("Attributes Table Service update success");
                    } else {
                        serviceResponseDTO.setDescription("Attributes Table Service update Fail attribute Not Found");
                    }
                    serviceResponseDTO.setMessage(SUCCESS);
                    serviceResponseDTO.setCode(STATUS_2000);
                    serviceResponseDTO.setHttpStatus(STATUS_OK);
                } else {
                    AttributeTableEntity attributeTableEntity = new AttributeTableEntity();
                    attributeTableEntity.setDisplayName(attributeTableDTO.getDisplayName());
                    attributeTableEntity.setAttributeTableId(new AttributeTableId(attributeTableDTO.getTableName().toLowerCase(), attributeTableDTO.getOrganizationUuid()));
                    attributeTableEntity = attributeTableRepository.save(attributeTableEntity);
                    serviceResponseDTO.setDescription("Attributes Table Service save success");
                    attributeTableSaveUpdateResponseDTO.setTableName(attributeTableEntity.getAttributeTableId().getAttributeTableName());
                    attributeTableSaveUpdateResponseDTO.setDisplayName(attributeTableEntity.getDisplayName());
                    serviceResponseDTO.setData(attributeTableSaveUpdateResponseDTO);
                    serviceResponseDTO.setMessage(SUCCESS);
                    serviceResponseDTO.setCode(STATUS_2000);
                    serviceResponseDTO.setHttpStatus(STATUS_OK);
                }
            } else {
                serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND);
            }
        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveAttributeTable() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
        }
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getAttributes(AttributeTableRequestDTO attributeTableDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AttributesTableServiceImpl getAttributes()");
        try {
            AttributeTableResponseDTO attributeTableResponseDTO = new AttributeTableResponseDTO();
            List<AttributeTableEntity> stream;
            if (attributeTableDTO.isPagination()) {
                // With Pagination
                stream = attributeTableRepository.findAllByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameStartingWith(attributeTableDTO.getOrganizationUuid(), attributeTableDTO.getName().toLowerCase(), PageRequest.of(attributeTableDTO.getPage(), attributeTableDTO.getSize()));
                serviceResponseDTO.setMessage("AttributesTableServiceImpl Get Attributes With Pagination Success");
            } else {
                // Without Pagination
                stream = attributeTableRepository.findAllByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableIdOrganizationUuidEquals(attributeTableDTO.getName().toLowerCase(), attributeTableDTO.getOrganizationUuid());
                serviceResponseDTO.setMessage("AttributesTableServiceImpl Get Attributes Without Pagination Success");
            }
            attributeTableResponseDTO.setAttributeTableEntities(stream);
            attributeTableResponseDTO.setCount(attributeTableRepository.countByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEquals(attributeTableDTO.getName().toLowerCase(), attributeTableDTO.getOrganizationUuid()));
            serviceResponseDTO.setData(attributeTableResponseDTO);
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: AttributesTableServiceImpl getAttributes() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("AttributesTableServiceImpl getAttributes() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO activeInactiveAttributeTable(String attributeTableName, String organizationUuid, boolean status) {
        log.info("LOG:: AttributesTableServiceImpl activeInactiveAttributeTable");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationEntityByUuidEquals(organizationUuid);
            if (organizationEntity.isPresent()) {
                Optional<AttributeTableEntity> attributeTableEntity = attributeTableRepository.findByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(attributeTableName.toLowerCase(), organizationUuid);
                if (attributeTableEntity.isPresent()) {
                    attributeTableEntity.get().setStatus(status);
                    serviceResponseDTO.setData(attributeTableRepository.save(attributeTableEntity.get()));
                    serviceResponseDTO.setDescription("AttributesTableServiceImpl activeInactiveAttributeTable() Success");
                } else {
                    serviceResponseDTO.setDescription("AttributesTableServiceImpl activeInactiveAttributeTable() attribute Not Found");
                }
            } else {
                serviceResponseDTO.setDescription("AttributesTableServiceImpl activeInactiveAttributeTable() Organization Not Found");
            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
        } catch (Exception exception) {
            log.info("LOG :: AttributesTableServiceImpl activeInactiveAttributeTable() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("AttributesTableServiceImpl activeInactiveAttributeTable() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
        }
        return serviceResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getSingleAttributeTable(AttributeTableGetSingleDTO attributeRequestDTO) {
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            Optional<AttributeTableEntity> attributeTableEntity = attributeTableRepository.findByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameEquals(attributeRequestDTO.getOrganizationUuid(), attributeRequestDTO.getAttributeTableName().toLowerCase());
            if (attributeTableEntity.isPresent()) {
                serviceGetResponseDTO.setData(attributeTableEntity.get());
                serviceGetResponseDTO.setDescription("Get Attribute Table Success");
            } else {
                serviceGetResponseDTO.setDescription("Cannot Find Attribute Table");
            }
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl getSingleAttributeTable() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("AttributesServiceImpl getSingleAttributeTable() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }
}
