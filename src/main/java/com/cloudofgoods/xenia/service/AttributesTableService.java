package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.AttributeTableDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface AttributesTableService {
    ServiceResponseDTO saveOrUpdateAttributeTable(AttributeTableDTO attributeTableDTO);

    ServiceGetResponseDTO getAttributesTables(AttributeTableRequestDTO attributeTableDTO);

    ServiceResponseDTO activeInactiveAttributeTable(String attributeTableName, String organizationUuid, boolean status);

    ServiceGetResponseDTO getSingleAttributeTable(AttributeTableGetSingleDTO attributeRequestDTO);
}
