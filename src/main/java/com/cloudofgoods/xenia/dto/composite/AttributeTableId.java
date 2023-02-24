package com.cloudofgoods.xenia.dto.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeTableId {
    private String attributeTableName;
    private String organizationUuid;

}
