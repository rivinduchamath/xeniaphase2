package com.cloudofgoods.xenia.service;

import org.kie.api.definition.KiePackage;

import java.util.Collection;

public interface DroolService {
    Collection<KiePackage> getAllRuleFromKnowledgeBase();
}
