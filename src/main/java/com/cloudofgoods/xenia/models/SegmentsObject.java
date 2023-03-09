package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentsObject {

    @Indexed(unique = true)
    @NotEmptyOrNull
    private String segmentName;
    @TextIndexed
    private String segmentDescription;
    private double priority;
    @TextIndexed
    @NotEmptyOrNull
    private String segmentRuleString;

    private boolean template;
    @NotEmptyOrNull
    private String channelId;
    private NodeObject ruleObject;
    private List <ExperiencesObject> experiences;
}
