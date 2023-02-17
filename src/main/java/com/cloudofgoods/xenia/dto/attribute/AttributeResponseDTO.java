package com.cloudofgoods.xenia.dto.attribute;

import com.cloudofgoods.xenia.entity.SuperEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
public class AttributeResponseDTO implements SuperEntity {

    private String attributeName;
    private String displayName;
    private String type;
    private List <Object> values;
    private String uuid;
}

