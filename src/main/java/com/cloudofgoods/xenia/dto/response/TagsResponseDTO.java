package com.cloudofgoods.xenia.dto.response;

import com.cloudofgoods.xenia.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagsResponseDTO implements SuperEntity {

    private String tagsName;
    private String organizationUuid;
    private boolean status;
}

