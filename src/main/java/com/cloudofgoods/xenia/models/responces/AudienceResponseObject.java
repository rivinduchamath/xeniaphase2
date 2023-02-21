package com.cloudofgoods.xenia.models.responces;

import com.cloudofgoods.xenia.entity.xenia.AudienceEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudienceResponseObject {
    private List<AudienceEntity> audienceEntities;
    private long total;
}
