package com.cloudofgoods.xenia.models.composite;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChannelsId {
    private String organization;
    private String channelsName;
}
