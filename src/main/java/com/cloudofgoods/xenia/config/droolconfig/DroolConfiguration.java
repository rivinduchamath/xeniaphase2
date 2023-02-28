package com.cloudofgoods.xenia.config.droolconfig;

import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderConfiguration;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DroolConfiguration {
    @Bean
    public KnowledgeBuilderConfiguration knowledgeBuilderConfiguration() {return KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();}
    @Bean
    public KnowledgeBuilder knowledgeBuilder() {
        return KnowledgeBuilderFactory.newKnowledgeBuilder();
    }
    @Bean
    public InternalKnowledgeBase internalKnowledgeBase() {
        return KnowledgeBaseFactory.newKnowledgeBase();
    }

}
