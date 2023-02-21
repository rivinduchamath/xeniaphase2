package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.AttributeTableDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface AttributesTableService {
    ServiceResponseDTO saveOrUpdateAttributeTable(AttributeTableDTO attributeTableDTO);

    ServiceResponseDTO getAttributes(AttributeTableRequestDTO attributeTableDTO);
}
