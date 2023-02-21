package com.cloudofgoods.xenia.dto.request;

import com.cloudofgoods.xenia.dto.composite.ChannelsIdDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelsRequestDTO {
    private ChannelsIdDTO channelsIdDTO;
    private String uuid;
}
