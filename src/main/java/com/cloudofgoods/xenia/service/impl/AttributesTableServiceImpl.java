package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.AttributeTableDTO;
import com.cloudofgoods.xenia.dto.composite.AttributeTableId;
import com.cloudofgoods.xenia.dto.request.AttributeTableRequestDTO;
import com.cloudofgoods.xenia.dto.response.AttributeTableResponseDTO;
import com.cloudofgoods.xenia.dto.response.AttributeTableSaveUpdateResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.repository.AttributeTableRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AttributesTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttributesTableServiceImpl implements AttributesTableService {
    private final AttributeTableRepository attributeTableRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateAttributeTable(AttributeTableDTO attributeTableDTO) {

        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            if (attributeTableDTO.getOrganizationUuid() != null) {
               Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationEntityByUuidEquals(attributeTableDTO.getOrganizationUuid());
                if (organizationEntity.isPresent()) {
                    if (attributeTableDTO.getId() != null) {
                        Optional<AttributeTableEntity> save = attributeTableRepository.findAllByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(attributeTableDTO.getId(), attributeTableDTO.getOrganizationUuid());
                        if (save.isPresent()) {
                            save.get().setAttributeTableId(new AttributeTableId(attributeTableDTO.getDisplayName(), attributeTableDTO.getOrganizationUuid()));
                            AttributeTableEntity save1 = attributeTableRepository.save(save.get());
                            AttributeTableSaveUpdateResponseDTO attributeTableSaveUpdateResponseDTO = new AttributeTableSaveUpdateResponseDTO();
                            attributeTableSaveUpdateResponseDTO.setTableName(save1.getAttributeTableId().getAttributeTableName().toLowerCase());
                            attributeTableSaveUpdateResponseDTO.setDisplayName(save1.getDisplayName());
                            serviceResponseDTO.setData(attributeTableSaveUpdateResponseDTO);
                            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveOrUpdateAttributeTable() update success");
                        } else {
                            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveOrUpdateAttributeTable() attribute Not Found");
                        }
                        serviceResponseDTO.setData(save);
                        serviceResponseDTO.setMessage("success");
                        serviceResponseDTO.setCode("2000");
                        serviceResponseDTO.setHttpStatus("OK");
                        return serviceResponseDTO;
                    } else {
                        AttributeTableEntity attributeTableEntity = new AttributeTableEntity();
                        attributeTableEntity.setDisplayName(attributeTableDTO.getDisplayName());
                        attributeTableEntity.setAttributeTableId(new AttributeTableId(attributeTableDTO.getTableName().toLowerCase(), attributeTableDTO.getOrganizationUuid()));
                        AttributeTableEntity save = attributeTableRepository.save(attributeTableEntity);
                        serviceResponseDTO.setDescription("AttributesTableServiceImpl saveOrUpdateAttributeTable() save success");
                        AttributeTableSaveUpdateResponseDTO attributeTableSaveUpdateResponseDTO = new AttributeTableSaveUpdateResponseDTO();
                        attributeTableSaveUpdateResponseDTO.setTableName(save.getAttributeTableId().getAttributeTableName().toLowerCase());
                        attributeTableSaveUpdateResponseDTO.setDisplayName(save.getDisplayName());
                        serviceResponseDTO.setData(attributeTableSaveUpdateResponseDTO);
                        serviceResponseDTO.setMessage("success");
                        serviceResponseDTO.setCode("2000");
                        serviceResponseDTO.setHttpStatus("OK");
                        return serviceResponseDTO;
                    }
                }else {
                    serviceResponseDTO.setDescription("Organization Not Found");
                }
            } else {
                serviceResponseDTO.setDescription("Organization cannot be empty ");
            }
        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.getStackTrace();
            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveAttributeTable() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");

        }
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getAttributes(AttributeTableRequestDTO attributeTableDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AttributesTableServiceImpl getAttributes()");
        try {
            AttributeTableResponseDTO attributeTableResponseDTO = new AttributeTableResponseDTO();
            Page<AttributeTableEntity> stream = null;
            long totalCount;
            if (attributeTableDTO.getName() != null && !attributeTableDTO.getName().equals("")) {
                stream = attributeTableRepository.findAllByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEquals(
                        attributeTableDTO.getName().toLowerCase(), attributeTableDTO.getOrganizationUuid(),PageRequest.of(attributeTableDTO.getPage(), attributeTableDTO.getSize()));
                totalCount = attributeTableRepository.countByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEquals(
                        attributeTableDTO.getName().toLowerCase(), attributeTableDTO.getOrganizationUuid());

            } else {
                stream = attributeTableRepository.findAllByAttributeTableId_OrganizationUuidEquals(
                          attributeTableDTO.getOrganizationUuid(),PageRequest.of(attributeTableDTO.getPage(), attributeTableDTO.getSize()));
                totalCount = attributeTableRepository.countByAttributeTableId_OrganizationUuidEquals( attributeTableDTO.getOrganizationUuid());
            }
            attributeTableResponseDTO.setAttributeTableEntities(stream.getContent());
            attributeTableResponseDTO.setCount(totalCount);
            serviceResponseDTO.setData(attributeTableResponseDTO);
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: AudienceServiceImpl getAudienceWithPagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }
}
