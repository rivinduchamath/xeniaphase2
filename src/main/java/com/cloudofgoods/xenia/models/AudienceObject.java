package com.cloudofgoods.xenia.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AudienceObject implements Serializable {

    private String audienceName;
    @TextIndexed
    private String audienceDescription;
    @TextIndexed
    private String audienceRuleString;
    private List <SegmentsObject> segments;
    private boolean template;
    private NodeObject audienceObject;
}
