package com.cloudofgoods.xenia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRequestTagsDTO {

    private int page;
    private int size;
    private String organizationUuid;
    private String tagsName;
}
