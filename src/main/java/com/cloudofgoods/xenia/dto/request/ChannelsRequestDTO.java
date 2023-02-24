package com.cloudofgoods.xenia.dto.request;

import com.cloudofgoods.xenia.dto.composite.ChannelsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelsRequestDTO {
    private ChannelsId channelsId;
    private String uuid;
}
