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
@Document(collection = "organization_analytics_entity")
public class OrganizationAnalyticsEntity {
    @Id
    private String name;
    private List<CampaignsObjects> allCampaign;
    private long activeCampaignCount;
    private long organizationTotalRequestCount;
    private long organizationTotalResponseCount;
    private long organizationMatchResponses;
}
