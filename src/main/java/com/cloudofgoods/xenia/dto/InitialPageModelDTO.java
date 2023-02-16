package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.models.SegmentsObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitialPageModelDTO {
    private String campaignId;
    private List<SegmentsObject> segmentNames;
    private String campaignName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
    private Date createdDate;

}
