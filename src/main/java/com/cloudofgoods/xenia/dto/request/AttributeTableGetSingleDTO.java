package com.cloudofgoods.xenia.dto.request;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AttributeTableGetSingleDTO {
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    private String organizationUuid;
    @NotEmptyOrNull(message = "Attribute Table Name Must Not Be Empty")
    private String attributeTableName;

}

