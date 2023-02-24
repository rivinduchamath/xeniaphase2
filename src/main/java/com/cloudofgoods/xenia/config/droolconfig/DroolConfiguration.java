package com.cloudofgoods.xenia.config.droolconfig;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderConfiguration;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolConfiguration {

    @Bean
    public KnowledgeBuilderConfiguration knowledgeBuilderConfiguration() {
        return KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
    }

    @Bean
    public KnowledgeBuilder knowledgeBuilder() {
        return KnowledgeBuilderFactory.newKnowledgeBuilder();
    }

    @Bean
    public InternalKnowledgeBase internalKnowledgeBase() {
        return KnowledgeBaseFactory.newKnowledgeBase();
    }
}
