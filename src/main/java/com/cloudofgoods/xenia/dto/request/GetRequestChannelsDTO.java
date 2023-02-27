package com.cloudofgoods.xenia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRequestChannelsDTO {
    private int page;
    private int size;
    private String organizationUuid;
    private String channelName;

}