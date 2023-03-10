package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.xenia.SegmentTemplateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateCustomObject {

    private List <SegmentTemplateEntity> templateEntities;


}
