package com.cloudofgoods.xenia.models;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;
import java.util.List;


@Data
public class CustomerEntityObject {

    private String segmentRuleString;
    private double priority;
    @Indexed(unique = true)
    private String segmentName;
    private String variant;
    private boolean abTest;
    private Date abTestEndDateTime;
    private Date endDateTime;
    private Date startDateTime;
    @TextIndexed
    private String segmentationDescription;
    private double abTestTraffic;
    private String slotId;
    private String organization;
    @TextIndexed
    private List<String> channel;
    private NodeObject fact;
    private ChannelContentObject channelContentObject;
}
