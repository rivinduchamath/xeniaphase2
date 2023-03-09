package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleChannelObject {
    @NotEmptyOrNull
    private String channelId;
    @NotEmptyOrNull
    private String channelType;
    private List<AudienceObject> audienceObjects;

}

