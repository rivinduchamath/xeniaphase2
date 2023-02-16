package com.cloudofgoods.xenia.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class AudienceObject implements Serializable {

    private String audienceName;
    @TextIndexed
    private String audienceDescription;
    private List <SegmentsObject> segments;
}
