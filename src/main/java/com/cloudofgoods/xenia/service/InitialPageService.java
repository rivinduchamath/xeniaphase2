package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.request.InitialPageRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface InitialPageService {
    ServiceResponseDTO getCampaignForInitialPage(InitialPageRequestDTO initialPageRequestDTO);
}
