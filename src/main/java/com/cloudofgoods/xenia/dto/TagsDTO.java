package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagsDTO {

    @NotEmptyOrNull(message = "Organization Uuid Cannot be Empty")
    private String organizationUuid;
    @NotEmptyOrNull(message = "Tags Name Cannot be Empty")
    private String tagsName;
    private boolean status;

}
