package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.RuleChannelObject;
import com.cloudofgoods.xenia.util.RuleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "rule_request_root_model")
public class RuleRequestRootEntity implements SuperEntity {
    @Id
    private String id;
    private String campaignName;
    private String campaignId;
    private String campaignDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
    private Date startDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
    private Date endDateTime;
    private Integer priority;
    private List <String> channelIds;
    private List <String> tags;
    private List <RuleChannelObject> channels;
    @Indexed
    private RuleStatus statusEnum;
    private String organizationId;
    private String creator;
    private String updater;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
    private Date updatedDate;

}
