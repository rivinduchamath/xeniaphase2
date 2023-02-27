package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.request.AttributeGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface AttributesService {
    ServiceResponseDTO saveAttribute(AttributeRequestDTO attributesDTO);


    ServiceResponseDTO activeInactiveAttribute(String attributeId, String organizationUuid, boolean status);

    ServiceGetResponseDTO getAttribute(GetRequestAttributeDTO getRequestAttributeDTO);

    ServiceGetResponseDTO getSingleAttribute(AttributeGetSingleDTO attributeRequestDTO);
}
