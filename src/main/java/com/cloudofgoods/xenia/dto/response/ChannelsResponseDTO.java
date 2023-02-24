package com.cloudofgoods.xenia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelsResponseDTO {
    private String channelName;
    private String channelUuid;
    private String type;
}
