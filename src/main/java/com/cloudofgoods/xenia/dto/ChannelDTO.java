package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDTO {
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    private String organizationUuid;
    @NotEmptyOrNull(message = "Channel Name Must Not Be Empty")
    private String channelName;
    private String channelUuid;
    @NotEmptyOrNull(message = "Type Must Not Be Empty")
    private String type;
    private boolean status;

}
