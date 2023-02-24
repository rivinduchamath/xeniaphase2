package com.cloudofgoods.xenia.dto.response;

import com.cloudofgoods.xenia.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeResponseDTO implements SuperEntity {

    private String attributeName;
    private String displayName;
    private String type;
    private List <Object> values;
    private String attributeUuid;
    private String tableName;
}

