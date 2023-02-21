package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.AttributeTableDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableRequestDTO;
import com.cloudofgoods.xenia.dto.response.AttributeTableResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import com.cloudofgoods.xenia.repository.AttributeTableRepository;
import com.cloudofgoods.xenia.service.AttributesTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttributesTableServiceImpl implements AttributesTableService {
    private final AttributeTableRepository attributeTableRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateAttributeTable(AttributeTableDTO attributeTableDTO) {

        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            if (attributeTableDTO.getId() != null) {
                Optional<AttributeTableEntity> save = attributeTableRepository.findById(attributeTableDTO.getId());
                        if (save.isPresent()){
                            save.get().setDisplayName(attributeTableDTO.getDisplayName());
                            attributeTableRepository.save(save.get());
                            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveOrUpdateAttributeTable() update success");
                        }else {
                            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveOrUpdateAttributeTable() attribute Not Found");
                        }
                serviceResponseDTO.setData(save);
                serviceResponseDTO.setMessage("success");
                serviceResponseDTO.setCode("2000");
                serviceResponseDTO.setHttpStatus("OK");
                return serviceResponseDTO;
            }else {
                AttributeTableEntity attributeTableEntity = new AttributeTableEntity();
                attributeTableEntity.setAttributeTableName(attributeTableDTO.getTableName());
                attributeTableEntity.setDisplayName(attributeTableDTO.getDisplayName());
                AttributeTableEntity save = attributeTableRepository.save(attributeTableEntity);
                serviceResponseDTO.setDescription("AttributesTableServiceImpl saveOrUpdateAttributeTable() save success");
                serviceResponseDTO.setData(save);
                serviceResponseDTO.setMessage("success");
                serviceResponseDTO.setCode("2000");
                serviceResponseDTO.setHttpStatus("OK");
                return serviceResponseDTO;
            }

        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.getStackTrace();
            serviceResponseDTO.setDescription("AttributesTableServiceImpl saveAttributeTable() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO getAttributes(AttributeTableRequestDTO attributeTableDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AttributesTableServiceImpl getAttributes()");
        try {
            AttributeTableResponseDTO attributeTableResponseDTO = new AttributeTableResponseDTO();
            List<AttributeTableEntity> stream = attributeTableRepository.findAllByAttributeTableNameEquals(attributeTableDTO.getName(), PageRequest.of(attributeTableDTO.getPage(), attributeTableDTO.getSize()));
            long totalCount = attributeTableRepository.countByAttributeTableNameEquals(attributeTableDTO.getName());
            attributeTableResponseDTO.setAttributeTableEntities(stream);
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
            serviceResponseDTO.setMessage("AudienceServiceImpl getAudienceWithPagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }
}
