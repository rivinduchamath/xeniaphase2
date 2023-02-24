package com.cloudofgoods.xenia.models.composite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelsId {

    private String organizationUuid;
    private String channelsName;
}
