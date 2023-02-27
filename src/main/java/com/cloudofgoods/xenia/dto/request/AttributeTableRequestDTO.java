package com.cloudofgoods.xenia.dto.request;

import com.cloudofgoods.xenia.config.customAnnotations.NonNegative;
import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeTableRequestDTO {
    private String name;
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    private String organizationUuid;
    @NonNegative
    private int page;
    @NonNegative
    private int size;
    private boolean pagination;
    private boolean status;
}

