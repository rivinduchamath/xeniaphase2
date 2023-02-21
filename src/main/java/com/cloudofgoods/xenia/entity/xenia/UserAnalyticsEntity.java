package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user_analytics")
@Data
public class UserAnalyticsEntity implements SuperEntity {
    @Id
    @Indexed
    private String userEmail;
    private long successRulesCount;

    @Indexed(expireAfterSeconds = 11, direction = IndexDirection.DESCENDING)
    private long matchedRulesCount;
    private String organizationName;
    List<String> satisfiedConditionsName =  new ArrayList<>();
}
