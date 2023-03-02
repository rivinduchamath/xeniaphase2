package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.util.Utils;
import com.cloudofgoods.xenia.dto.D6nResponseModelDTO;
import com.cloudofgoods.xenia.dto.caution.MetaData;
import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.UserAnalyticsEntity;
import com.cloudofgoods.xenia.repository.redis.ResponseUserRepository;
import com.cloudofgoods.xenia.service.D6nService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class D6nServiceImpl implements D6nService {

//    private final ResponseUserRepository responseUserRepository;
    @Autowired
    private InternalKnowledgeBase internalKnowledgeBase;

    @Override
    public ServiceResponseDTO makeDecision(int numberOfResponseFrom, int numberOfResponse, String userEmail, User user, List<String> channel, List<String> slot, String organization) {
        log.info("LOG :: D6nServiceImpl makeDecision() Set Meta Data");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss.SSSZ");
            Date date = new Date();
            NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
            UUID firstUUID = timeBasedGenerator.generate();
            List<D6nResponseModelDTO> d6nResponseModelDTO = new ArrayList<>();
            slot.forEach(s -> {
                MetaData metaData = new MetaData();
                metaData.setEndDate(Utils.today);
                metaData.setStartDate(Utils.today);
                metaData.setContextId(Collections.singletonList(s.toUpperCase()));
                metaData.setChannels(channel);
                D6nResponseModelDTO d6nResponseModelDTO1 = makeDecision(numberOfResponseFrom, numberOfResponse, metaData, user, organization.toUpperCase(), (dateFormat.format(date) + "##$$##" + firstUUID));
                if(d6nResponseModelDTO1.getVariant() == null) d6nResponseModelDTO1.setVariant("null");
                if(d6nResponseModelDTO1.getSlotId() == null) d6nResponseModelDTO1.setSlotId(s);
                d6nResponseModelDTO.add(d6nResponseModelDTO1);
            });
//            CompletableFuture.runAsync(() -> databaseAnalytics(d6nResponseModelDTO, userEmail, organization));
            serviceResponseDTO.setData(d6nResponseModelDTO);
            serviceResponseDTO.setDescription("makeDecision Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl saveTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
        }
        return serviceResponseDTO;
    }

//    private void databaseAnalytics(D6nResponseModelDTO d6nResponseModelDTO, String userEmail, String organization) {
//        UserAnalyticsEntity userAnalyticsEntity = new UserAnalyticsEntity();
//        userAnalyticsEntity.setUserEmail(userEmail);
//        userAnalyticsEntity.setOrganizationName(organization);
//
//        userAnalyticsEntity.setMatchedRulesCount(d6nResponseModelDTO.getSatisfiedConditions().size());
//        userAnalyticsEntity.setSatisfiedConditionsName(d6nResponseModelDTO.getSatisfiedConditions());
////        String createdAt = mongoTemplate.indexOps(UserAnalyticsEntity.class).ensureIndex(new Index().on("satisfiedConditionsName", Sort.Direction.ASC).expire(10));
//        UserAnalyticsEntity save = responseUserRepository.save(userAnalyticsEntity);
//
//    }

    //Remove Rules From Knowledge Base And Database From SegmentsObject ID
    public D6nResponseModelDTO makeDecision(int numberOfResponseFrom, int numberOfResponse, MetaData metaData, User user, String organization, String uuid) {
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
        // Add listener to retrieve satisfied conditions
//        List<String> satisfiedConditionsName =  new ArrayList<>();
//        kieSession.addEventListener(new DefaultAgendaEventListener() {
//
//            @Override
//            public void afterMatchFired(AfterMatchFiredEvent event) {
//
//                super.afterMatchFired(event);
//                List<Object> name = event.getMatch().getObjects();
////                List<Object> namea = Collections.singletonList(event.getMatch().getFactHandles());
//            }
//        });
        int ruleCount = kieSession.fireAllRules();
        log.info("LOG:: DroolServiceImpl makeDecision Executed " + ruleCount + " rules.");
        kieSession.dispose();

        List<String> subList = d6nResponse.getSatisfiedConditions();
        numberOfResponse = Math.min(numberOfResponse, subList.size());

        Collections.reverse(subList);
        d6nResponse.setSatisfiedConditions( subList.subList(numberOfResponseFrom, numberOfResponse));
        d6nResponse.setTotalCount(ruleCount);
        return d6nResponse;
    }
}