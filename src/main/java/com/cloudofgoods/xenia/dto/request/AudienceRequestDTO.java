package com.cloudofgoods.xenia.dto.request;

import com.cloudofgoods.xenia.config.customAnnotations.NonNegative;
import com.cloudofgoods.xenia.config.customAnnotations.NonZero;
import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudienceRequestDTO {
    private String audienceName;
    @NotEmptyOrNull(message = "Organization Id Must Not Be Empty")
    private String organizationId;
    @NonNegative
    private int page;
    @NonZero
    private int size;
    private boolean pagination;

}
