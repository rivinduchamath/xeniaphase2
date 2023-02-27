package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.request.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface AttributesService {
    ServiceResponseDTO saveAttribute(AttributeRequestDTO attributesDTO);


    ServiceResponseDTO deleteAttribute(String attributeId, String organizationUuid);

    ServiceGetResponseDTO getAttribute(GetRequestAttributeDTO getRequestAttributeDTO);
}
