package com.cloudofgoods.xenia.entity.xenia.analytics;

import com.cloudofgoods.xenia.models.CampaignsObjects;
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
@Document(collection = "campaign_entity")
public class OrganizationAnalyticsEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private List<CampaignsObjects> allCampaign;
    private long activeCampaignCount;
    private long scheduledCampaignCount;
    private long expiredOneWeekCount;
    private long organizationTotalRequestCount;
    private long organizationTotalResponseCount;
    private long organizationMatchResponses;
}
