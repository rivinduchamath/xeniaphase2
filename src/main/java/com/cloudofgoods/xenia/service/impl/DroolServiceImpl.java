package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.D6nResponseModelDTO;
import com.cloudofgoods.xenia.dto.caution.MetaData;
import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import com.cloudofgoods.xenia.models.*;
import com.cloudofgoods.xenia.repository.RootRuleRepository;
import com.cloudofgoods.xenia.service.DroolService;
import com.cloudofgoods.xenia.util.RuleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.lang.api.DescrFactory;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.mvel.DrlDumper;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableCaching
public class DroolServiceImpl extends RuleImpl implements DroolService {
    @Autowired
    private KnowledgeBuilder knowledgeBuilder;
    @Value("${knowledge.package.name}")
    private String packageName;
    @Autowired
    private InternalKnowledgeBase internalKnowledgeBase;

    @Autowired
    private RootRuleRepository rootRuleRepository;

    @Override
    public Collection<KiePackage> getAllRuleFromKnowledgeBase() {
        return knowledgeBuilder.getKnowledgePackages().stream()
                .peek(kiePackage -> log.info("LOG::DroolServiceImpl getAllRuleFromKnowledgeBase() collection : " + kiePackage.toString()))
                .collect(Collectors.toList());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadKnowledgeBuilder() {
        log.info("LOG:: DroolServiceImpl loadKnowledgeBuilder() Get All Rules From Database");
        rootRuleRepository.findByStatusEnumAndEndDateTimeGreaterThan(RuleStatus.ACTIVE, new Date()).forEach(this::feedKnowledge);
    }

    public void feedKnowledgeBuilderWhenUpdate(RuleRequestRootEntity allRuleSet, RuleRequestRootEntity pastRule) {
        log.info("LOG:: DroolServiceImpl loadKnowledgeBuilder() Get All Rules From Database");
        pastRule.getChannels().stream().flatMap(channelsEntity -> channelsEntity.getAudienceObjects().stream())
                .flatMap(audienceObject -> audienceObject.getSegments().stream()).map(SegmentsObject::getSegmentName).forEach(this::removeFromKB);
        log.info("LOG:: DroolServiceImpl loadKnowledgeBuilder() after for");
        feedKnowledge(allRuleSet);
    }

    public void removeFromKB(String ruleName) {
        log.info("LOG:: RuleServiceImpl removeFromKB() : " + ruleName);
        Optional.ofNullable(ruleName).ifPresent(name -> {
            log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase() rule Is Present ");
            internalKnowledgeBase.removeRule(packageName, name);
        });

    }

    public void feedKnowledge(RuleRequestRootEntity allRuleSet) {
        log.info("LOG:: DroolServiceImpl feedKnowledge() ");
        for (RuleChannelObject ruleChannelObject : allRuleSet.getChannels()) {
            for (AudienceObject audienceObject : ruleChannelObject.getAudienceObjects()) {
                for (SegmentsObject segmentsObject : audienceObject.getSegments()) {
                    for (ExperiencesObject experiencesObject : segmentsObject.getExperiences()) {
                        for (ChannelContentObject channelContentObject : experiencesObject.getEntryVariantMapping()) {
                            knowledgeBuilder.add(ResourceFactory.newByteArrayResource(channelContentObject.getFullRuleString().getBytes()), ResourceType.DRL);
                            KnowledgeBuilderErrors errors = knowledgeBuilder.getErrors();
                            if (errors.size() > 0) {
                                errors.forEach(error -> {
                                    log.error("Log ::DroolServiceImpl Error In Feed To the Knowledge builder. Error Message " + error.getMessage());
                                    log.error("Log :: DroolServiceImpl Error Size " + errors.size());
                                    try {
                                        knowledgeBuilder.undo();
                                        removeRuleFromKBAndDatabase(allRuleSet.getId());
                                    } catch (ExecutionException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                throw new IllegalArgumentException("Could not parse knowledge.");
                            } else {
                                // Add To Knowledge Base
                                log.info("Log :: DroolServiceImpl feedKnowledge() addPackages");
                                internalKnowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());
                                // Delete From Knowledge Builder
                            }
                            knowledgeBuilder.undo();
                        }
                    }
                }
            }
        }
    }

    public void removeRuleFromKBAndDatabase(String ruleId) throws ExecutionException, InterruptedException {
        try {
            log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase()" + ruleId);
            rootRuleRepository.deleteById(ruleId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

    }

//    //Find Root SegmentsObject With Multiple Child Rules From SegmentsObject ID
//    public RuleRequestRootEntity findRootRuleById(String ruleId) throws ExecutionException, InterruptedException {
//        log.info ("Find Root SegmentsObject By id " + ruleId);
//        return rootRuleRepository.findById (ruleId).get ();
//    }


    // Return Imports In Drool String
    public String createImports() {
        log.info("LOG:: DroolServiceImpl createImports");
        PackageDescr pkg = DescrFactory.newPackage().name("com.cloudofgoods.xenia")
                .newImport().target("com.cloudofgoods.xenia.dto.caution.User").end()
                .newImport().target("org.springframework.util.CollectionUtils").end()
                .newImport().target("com.cloudofgoods.xenia.dto.caution.MetaData").end()
                .newGlobal().type("com.cloudofgoods.xenia.dto.D6nResponseModelDTO").identifier("response").end().getDescr();
        DrlDumper dumper = new DrlDumper();
        return dumper.dump(pkg);
    }
}
