package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface InitialPageService {
    ServiceResponseDTO getCampaignForInitialPage(String slotId, int page, int size);
}
