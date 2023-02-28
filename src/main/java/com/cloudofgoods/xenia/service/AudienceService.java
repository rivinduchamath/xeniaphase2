package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.AudienceDTO;
import com.cloudofgoods.xenia.dto.request.AudienceGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AudienceRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AudienceEntity;

public interface AudienceService {
    ServiceResponseDTO saveAudience(AudienceDTO audienceDTO);
    ServiceResponseDTO getAudienceById(AudienceGetSingleDTO audienceUuid);

    ServiceResponseDTO getAudienceWithPagination(AudienceRequestDTO audienceRequestDTO);

    ServiceResponseDTO activeInactiveAudience(String audienceUuid, String organizationUuid, boolean status);
}
