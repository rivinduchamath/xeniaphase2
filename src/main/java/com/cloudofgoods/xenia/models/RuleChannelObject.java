package com.cloudofgoods.xenia.models;

import lombok.Data;

import java.util.List;

@Data
public class RuleChannelObject {
    private String channelId;
    private String channelType;
    private List<AudienceObject> audienceObjects;

}

