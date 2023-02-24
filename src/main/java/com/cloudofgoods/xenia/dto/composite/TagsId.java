package com.cloudofgoods.xenia.dto.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagsId {
    private String organizationUuid;
    private String tagsName;
}
