package com.cloudofgoods.xenia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagsDTO {

    private String organizationUuid;
    private String tagsName;

}
