package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeTableDTO {
    private String id;
    @NotEmptyOrNull(message = "Table Name Must Not Be Empty")
    private String tableName;
    private String displayName;
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    private String organizationUuid;

}
