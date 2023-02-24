package com.cloudofgoods.xenia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentsObject {

    @Indexed(unique = true)
    private String segmentName;
    @TextIndexed
    private String segmentDescription;
    private double priority;
    @TextIndexed
    private String segmentRuleString;
    private boolean template;
    @TextIndexed
    private String fullRuleString;
    private String channelId;
    private NodeObject ruleObject;
    private List <ChannelContentObject> entryVariantMapping;

}
