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
    private String channelName;
    private String channelUuid;
    private String type;
    private boolean status;

}
