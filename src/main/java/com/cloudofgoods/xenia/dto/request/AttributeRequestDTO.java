package com.cloudofgoods.xenia.dto.request;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AttributeRequestDTO {
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    private String organizationUuid;
    @NotEmptyOrNull(message = "Attribute Name Must Not Be Empty")
    private String attributeName;
    private String displayName;
    @NotEmptyOrNull(message = "Type Must Not Be Empty")
    private String type;
    private List<Object> values;
    private String attributeUuid;
    @NotEmptyOrNull(message = "Table Name Must Not Be Empty")
    private String tableName;
    private boolean status;
}

