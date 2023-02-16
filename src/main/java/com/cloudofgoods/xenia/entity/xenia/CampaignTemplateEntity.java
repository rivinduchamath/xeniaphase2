package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "campaign_template")
public class CampaignTemplateEntity implements SuperEntity {

    public String campaignDescription;
    public String campaignName;
    public String createdDate;
    public String creator;
    public String endDateTime;
    public String organization;
    public String slotId;
    public int priority;
    public String startDateTime;
    public String updater;
    @Indexed
    public String campTemplateName;
    @Id
    private String id;

}
