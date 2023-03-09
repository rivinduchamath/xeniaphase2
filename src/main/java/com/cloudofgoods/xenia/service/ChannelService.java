package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.request.ChannelsGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestChannelsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface ChannelService {
    ServiceResponseDTO saveOrUpdateChannel(ChannelDTO channelDTO);

    ServiceGetResponseDTO getChannels(GetRequestChannelsDTO getRequestChannelsDTO);

    ServiceResponseDTO activeOrInActiveChannels(String channelUuid, String organizationUuid, boolean status);

    ServiceGetResponseDTO getSingleChannelTable(ChannelsGetSingleDTO channelsGetSingleDTO);
}
