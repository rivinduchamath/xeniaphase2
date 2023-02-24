package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.OrganizationDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface OrganizationService {
    ServiceResponseDTO saveOrUpdateOrganization(OrganizationDTO organizationDTO);

    ServiceResponseDTO getOrganization();
}
