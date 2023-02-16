package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface ChannelService {
    ServiceResponseDTO saveChannel(ChannelDTO channelDTO);


    ServiceResponseDTO deleteChannels(String channelId);

    ServiceGetResponseDTO getChannels(int page, int size, String channelName, String organization);
}
