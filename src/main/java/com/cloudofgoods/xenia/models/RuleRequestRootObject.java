package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RuleRequestRootObject {
    private List <RuleRequestRootEntity> ruleRequestRootEntities;
    private long total;
}
