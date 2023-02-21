package com.cloudofgoods.xenia.dto.response;

import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeTableResponseDTO {
    private List<AttributeTableEntity> attributeTableEntities;
    private long count;
}
