package com.cloudofgoods.xenia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRequestChannelsDTO {
    private int page;
    private int size;
    private String organization;
    private String channelName;

}