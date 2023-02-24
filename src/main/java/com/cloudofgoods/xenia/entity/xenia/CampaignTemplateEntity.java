package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.RuleChannelObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "campaign_template")
public class CampaignTemplateEntity implements SuperEntity {

    @Id
    private String id;
    public String campaignDescription;
    public String campaignName;
    public String createdDate;
    public String creator;
    public String endDateTime;
    public String organization;
    public String startDateTime;
    public String updater;
    @Indexed
    public String campTemplateName;
    private List<String> channelIds;
    private List<String> tags;
    private List <RuleChannelObject> channels;

}
