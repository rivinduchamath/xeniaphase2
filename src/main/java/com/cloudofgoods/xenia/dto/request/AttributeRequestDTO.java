package com.cloudofgoods.xenia.dto.request;

import com.cloudofgoods.xenia.dto.composite.AttributeIdDTO;
import com.cloudofgoods.xenia.entity.SuperEntity;
import lombok.Data;
import java.util.List;

@Data
public class AttributeRequestDTO implements SuperEntity {

    private AttributeIdDTO attributeIdDTO;
    private String displayName;
    private String type;
    private List <Object> values;
    private String uuid;
    private String tableName;
}

