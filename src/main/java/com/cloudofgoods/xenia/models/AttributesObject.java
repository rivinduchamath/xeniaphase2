package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.models.composite.AttributesId;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@NoArgsConstructor
public class AttributesObject {


    private AttributesId attributesId;
    @Indexed
    private String displayName;
    @Indexed
    private String type;
    @Indexed
    private List<Object> values;
    @Indexed
    private String attributeUuid;

    private String tableName;


    public AttributesObject(String organization, String attributesId, String displayName, String type, List<Object> values, String attributeUuid, String tableName) {
        this.attributesId = new AttributesId(organization ,attributesId);
        this.displayName = displayName;
        this.type = type;
        this.values = values;
        this.attributeUuid = attributeUuid;
        this.tableName = tableName;
    }



}

