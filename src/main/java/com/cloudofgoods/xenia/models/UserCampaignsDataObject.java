package com.cloudofgoods.xenia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCampaignsDataObject {

    private long totalRulesCount;
    private long successRulesCount;
    private  long matchedRulesCount;
    private String organizationName;

}
