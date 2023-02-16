package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.AttributesObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class OrganizationDTO implements SuperEntity {

    private String id;
    private String name;
    private String uuid;
    private List <AttributesObject> attributesObject;
}