package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.xenia.TemplateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateCustomObject {

    private List <TemplateEntity> templateEntities;
    private long total;

}
