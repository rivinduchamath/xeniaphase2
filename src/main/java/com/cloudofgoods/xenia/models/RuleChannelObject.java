package com.cloudofgoods.xenia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleChannelObject {
    private String channelId;
    private String channelType;
    private List<AudienceObject> audienceObjects;

}

