package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.models.NodeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "audience_entity")
public class AudienceEntity {
    @Id
    private String audienceUuid;
    private String audienceName;
    @TextIndexed
    private String audienceDescription;
    @TextIndexed
    private String audienceRuleString;
    private String organizationUuid;
    private NodeObject audienceObject;
    private boolean status;
   }
