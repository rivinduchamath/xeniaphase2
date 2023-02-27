package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.AttributesObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO implements SuperEntity {

    private String id;
    @NotEmptyOrNull(message = "Organization name Must Not Be Empty")
    private String name;
    private String organizationUserName;
    private String organizationUuid;
    @NotEmptyOrNull(message = "Organization password Must Not Be Empty")
    private String password;
}
