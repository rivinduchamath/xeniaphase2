package com.cloudofgoods.xenia.entity.xenia;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "audience_entity")
public class AudienceEntity {
    @Id
    private String audienceUuid;
    private String audienceName;
    @TextIndexed
    private String audienceDescription;
    @TextIndexed
    private String audienceJson;
    @TextIndexed
    private String audienceRuleString;
    private String channelUuid;
    private String organizationUuid;
   }
