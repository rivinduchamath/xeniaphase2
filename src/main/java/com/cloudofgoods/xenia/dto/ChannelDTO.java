package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.dto.composite.ChannelsIdDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDTO {
    private ChannelsIdDTO channelsIdDTO;
    private String channelUuid;
}
