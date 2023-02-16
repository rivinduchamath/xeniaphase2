package com.cloudofgoods.xenia.models;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.List;


@Data
public class SegmentsObject {

    @Indexed(unique = true)
    private String segmentName;
    @TextIndexed
    private String segmentDescription;
    private double priority;
    @TextIndexed
    private String segmentRuleString;
    @TextIndexed
    private String fullRuleString;
    private String channelId;
    private NodeObject ruleObject;
    private List <ChannelContentObject> entryVariantMapping;

}
