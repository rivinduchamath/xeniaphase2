package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.composite.AttributesId;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@NoArgsConstructor
public class AttributesObject  {
    @Indexed
    private String attributeName;
    @Indexed
    private String displayName;
    @Indexed
    private String type;
    @Indexed
    private List <Object> values;
    @Indexed
    private String uuid;




    public AttributesObject(String  attributeName, String displayName, String type, List <Object> values, String uuid) {
        this.attributeName = attributeName;
        this.displayName = displayName;
        this.type        = type;
        this.values      = values;
        this.uuid        = uuid;
    }
}

