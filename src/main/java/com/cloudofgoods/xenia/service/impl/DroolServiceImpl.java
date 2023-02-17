package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.D6nResponseModelDTO;
import com.cloudofgoods.xenia.dto.caution.MetaData;
import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.entity.redis.ResponseRedisEntity;
import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import com.cloudofgoods.xenia.entity.xenia.TemplateEntity;
import com.cloudofgoods.xenia.models.AudienceObject;
import com.cloudofgoods.xenia.models.NodeObject;
import com.cloudofgoods.xenia.models.RuleChannelObject;
import com.cloudofgoods.xenia.models.SegmentsObject;
import com.cloudofgoods.xenia.repository.ChannelRepository;
import com.cloudofgoods.xenia.repository.RootRuleRepository;
import com.cloudofgoods.xenia.repository.TemplateRepository;
import com.cloudofgoods.xenia.repository.redis.ResponseRedisRepository;
import com.cloudofgoods.xenia.service.DroolService;
import com.cloudofgoods.xenia.util.RuleStatus;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.lang.api.DescrFactory;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.mvel.DrlDumper;
import org.kie.api.definition.KiePackage;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    @Autowired
    private ResponseRedisRepository responseRedisRepository;

    @Override
    public Collection<KiePackage> getAllRuleFromKnowledgeBase() {
        Collection<KiePackage> collection = knowledgeBuilder.getKnowledgePackages();
        log.info("LOG::DroolServiceImpl getAllRuleFromKnowledgeBase() collection Size : " + collection.size());
        return collection;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadKnowledgeBuilder() {
        log.info("LOG:: DroolServiceImpl loadKnowledgeBuilder() Get All Rules From Database");
        List<RuleRequestRootEntity> activeRule = rootRuleRepository.findByStatusEnumAndEndDateTimeGreaterThan(RuleStatus.ACTIVE, new Date());
        for (RuleRequestRootEntity ruleRequestRootEntity : activeRule) {
            feedKnowledge(ruleRequestRootEntity);
        }
    }

    public void feedKnowledgeBuilderWhenUpdate(RuleRequestRootEntity allRuleSet, RuleRequestRootEntity pastRule) {
        log.info("LOG:: DroolServiceImpl loadKnowledgeBuilder() Get All Rules From Database");
        for (RuleChannelObject channelsEntity : pastRule.getChannels()) {
            for (AudienceObject audienceObject : channelsEntity.getAudienceObjects()) {
                for (SegmentsObject segmentsObject : audienceObject.getSegments()) {
                    removeFromKB(segmentsObject.getSegmentName());
                }
            }
        }
        log.info("LOG:: DroolServiceImpl loadKnowledgeBuilder() after for");
        feedKnowledge(allRuleSet);
    }

    public void removeFromKB(String ruleName) {
        log.info("LOG:: RuleServiceImpl removeFromKB() : " + ruleName);
        if (ruleName != null) {
            log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase() rule Is Present ");
            internalKnowledgeBase.removeRule(packageName, ruleName);
        }
    }

    public void feedKnowledge(RuleRequestRootEntity allRuleSet) {
        log.info("LOG:: DroolServiceImpl feedKnowledge() ");
        for (RuleChannelObject ruleChannelObject : allRuleSet.getChannels()) {
            for (AudienceObject audienceObject : ruleChannelObject.getAudienceObjects()) {
                for (SegmentsObject segmentsObject : audienceObject.getSegments()) {
                    knowledgeBuilder.add(ResourceFactory.newByteArrayResource(segmentsObject.getFullRuleString().getBytes()), ResourceType.DRL);
                    KnowledgeBuilderErrors errors = knowledgeBuilder.getErrors();
                    if (errors.size() > 0) {
                        for (KnowledgeBuilderError error : errors) {
                            log.error("Log ::DroolServiceImpl Error In Feed To the Knowledge builder. Error Message " + error.getMessage());
                            log.error("Log :: DroolServiceImpl Error Size " + errors.size());
                            try {
                                knowledgeBuilder.undo();
                                removeRuleFromKBAndDatabase(allRuleSet.getId());
                            } catch (ExecutionException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        throw new IllegalArgumentException("Could not parse knowledge.");
                    } else {
                        // Add To Knowledge Base
                        log.info("Log :: DroolServiceImpl feedKnowledge() addPackages");
                        internalKnowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());
                        // Delete From Knowledge Builder
//                    log.info ("Log :: DroolServiceImpl feedKnowledge() undo >> Delete All in feedKnowledge");
                    }
                    knowledgeBuilder.undo();
                }
            }
        }
    }

    public void removeRuleFromKBAndDatabase(String ruleId) throws ExecutionException, InterruptedException {
        log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase()" + ruleId);
        rootRuleRepository.deleteById(ruleId);
        throw new IllegalArgumentException("Could not parse knowledge.");
    }

//    //Find Root SegmentsObject With Multiple Child Rules From SegmentsObject ID
//    public RuleRequestRootEntity findRootRuleById(String ruleId) throws ExecutionException, InterruptedException {
//        log.info ("Find Root SegmentsObject By id " + ruleId);
//        return rootRuleRepository.findById (ruleId).get ();
//    }

    //Remove Rules From Knowledge Base And Database From SegmentsObject ID
    @Cacheable(key = "#organization.concat('##$$##'+#uuid + '$$##$$'  +#metaData.channels +'%%$$%%'  +#metaData.contextId)", value = "organization"/*, unless = "#result.variant == null"*/)
    public D6nResponseModelDTO makeDecision(MetaData metaData, User user, String organization, String uuid) {
        log.info("Log :: DroolServiceImpl makeDecision()");
        log.info("Log :: DroolServiceImpl makeDecision() metaData : " + metaData.toString());
        log.info("Log :: DroolServiceImpl makeDecision() user : " + user.toString());
        log.info("Log :: DroolServiceImpl makeDecision() organization : " + organization);
        D6nResponseModelDTO d6nResponse = new D6nResponseModelDTO();
        KieSession kieSession = internalKnowledgeBase.newKieSession();

        Agenda agenda = kieSession.getAgenda();
        agenda.getAgendaGroup(organization).setFocus();
        kieSession.getGlobals().set("response", d6nResponse);
        kieSession.insert(user);

        kieSession.insert(metaData);
        List<ResponseRedisEntity> satisfiedConditions;
        satisfiedConditions = new ArrayList<>();

        // Add listener to retrieve satisfied conditions
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                ResponseRedisEntity ruleRequestRootEntity = new ResponseRedisEntity();
                super.afterMatchFired(event);
                String condition = event.getMatch().getRule().getName();
                ruleRequestRootEntity.setVariant(condition);
                satisfiedConditions.add(ruleRequestRootEntity);
            }
        });

        kieSession.fireAllRules();
        kieSession.dispose();
        CompletableFuture.runAsync(() -> responseRedisRepository.saveAll(satisfiedConditions));
        log.info("LOG:: DroolServiceImpl makeDecision d6nResponse " + d6nResponse);
        return d6nResponse;
    }

    // Return Imports In Drool String
    public String createImports() {
        log.info("LOG:: DroolServiceImpl createImports");
        PackageDescrBuilder pkg = DescrFactory.newPackage().name("com.cloudofgoods.xenia")
                .newImport().target("com.cloudofgoods.xenia.dto.caution.User").end()
                .newImport().target("org.springframework.util.CollectionUtils").end()
                .newImport().target("com.cloudofgoods.xenia.dto.caution.MetaData").end()
                .newGlobal().type("com.cloudofgoods.xenia.dto.D6nResponseModelDTO").identifier("response").end();
        PackageDescrBuilder pkgDescBuilder = pkg.end();
        PackageDescr packageDescr = pkgDescBuilder.getDescr();
        DrlDumper dumper = new DrlDumper();
        return dumper.dump(packageDescr);
    }
}
