package com.cloudofgoods.xenia.dto.response;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelsResponseDTO {
    @NotEmptyOrNull(message = "Channel Name Must Not Be Empty")
    private String channelName;
    private String channelUuid;
    @NotEmptyOrNull(message = "Type Must Not Be Empty")
    private String type;
}
