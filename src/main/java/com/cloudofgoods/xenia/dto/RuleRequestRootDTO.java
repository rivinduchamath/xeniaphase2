package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.models.RuleChannelObject;
import com.cloudofgoods.xenia.util.RuleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleRequestRootDTO {

    private String id;
    private String campaignName;
    private String campaignId;
    private String campaignDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
    private Date startDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
    private Date endDateTime;
    private String organizationId;
    private Integer priority;
    @Indexed
    private RuleStatus status;
    private List<String> channelIds;
    private List<String> tags;
    private List <RuleChannelObject> channels;

}
