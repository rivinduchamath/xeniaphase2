package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

import java.util.List;

public interface AttributesService {
    ServiceResponseDTO saveAttribute(AttributeRequestDTO attributesDTO);


    ServiceResponseDTO deleteAttribute(String attributeId, String organizationUuid);

    ServiceGetResponseDTO getAttribute(GetRequestAttributeDTO getRequestAttributeDTO);
}
