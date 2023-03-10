package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.D6nResponseModelDTO;
import com.cloudofgoods.xenia.dto.caution.MetaData;
import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.D6nService;
import com.cloudofgoods.xenia.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.cloudofgoods.xenia.util.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class D6nServiceImpl implements D6nService {

    @Autowired
    private InternalKnowledgeBase internalKnowledgeBase;

    @Override
    public ServiceResponseDTO makeDecision(int numberOfResponseFrom, int numberOfResponse, String userEmail, User userData, List<String> channel, List<String> slot, String organization) {
        log.info("LOG :: D6nServiceImpl makeDecision() One Set Meta Data");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            List<D6nResponseModelDTO> d6nResponseModelDTO = new ArrayList<>();
            slot.forEach(value -> {
                MetaData metaData = new MetaData();
                metaData.setEndDate(Utils.today);
                metaData.setStartDate(Utils.today);
                metaData.setContextId(Collections.singletonList(value.toUpperCase()));
                metaData.setChannels(channel.stream().map(String::toUpperCase).collect(Collectors.toList()));
                D6nResponseModelDTO d6nResponseModelDTO1 = makeDecision(numberOfResponseFrom, numberOfResponse, metaData, userData, organization.toUpperCase());
                if (d6nResponseModelDTO1.getVariant() == null) d6nResponseModelDTO1.setVariant("null");
                if (d6nResponseModelDTO1.getSlotId() == null) d6nResponseModelDTO1.setSlotId(value);
                d6nResponseModelDTO.add(d6nResponseModelDTO1);
            });
            serviceResponseDTO.setData(d6nResponseModelDTO);
            serviceResponseDTO.setDescription("makeDecision Success");
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl saveTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }
    Map<String, Double> VARIABLES_MAP = new HashMap<>();
    //Remove Rules From Knowledge Base And Database From SegmentsObject ID
    public D6nResponseModelDTO makeDecision(int numberOfResponseFrom, int numberOfResponse, MetaData metaData, User user, String organization) {
        log.info("Log :: DroolServiceImpl makeDecision() Two");
        log.info("Log :: DroolServiceImpl makeDecision() metaData : " + metaData.toString());
        log.info("Log :: DroolServiceImpl makeDecision() user : " + user.toString());
        D6nResponseModelDTO d6nResponse = new D6nResponseModelDTO();

        KieSession kieSession = internalKnowledgeBase.newKieSession();
        Agenda agenda = kieSession.getAgenda();
        agenda.getAgendaGroup(organization).setFocus();
        kieSession.getGlobals().set("response", d6nResponse);
        kieSession.insert(user);
        kieSession.insert(metaData);

        int ruleCount = kieSession.fireAllRules();
        log.info("LOG:: DroolServiceImpl makeDecision Executed " + ruleCount + " rules.");
        kieSession.dispose();

        List<String> subList = d6nResponse.getSatisfiedConditions();
        numberOfResponse = Math.min(numberOfResponse, subList.size());

        Collections.reverse(subList);
        d6nResponse.setSatisfiedConditions(subList.subList(numberOfResponseFrom, numberOfResponse));
        d6nResponse.setTotalCount(ruleCount);

        try {
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
            LocalDateTime startDate = LocalDateTime.parse(d6nResponse.getAbTestStartDate(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(d6nResponse.getAbTestEndDateTime(), formatter);
            if (ruleCount > 0){
                if (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {

                    Double storedValue = VARIABLES_MAP.getOrDefault(d6nResponse.getSegmentName(), 0.0);
                    double percentage = d6nResponse.getAbTestPercentage();
                    if (percentage == 0) {
                        return d6nResponse;
                    }
                    if (percentage > 50 && 100 >= percentage) {
                        double defaultVal = (100 - percentage) / percentage;
                        if (storedValue >= 1) {
                            VARIABLES_MAP.put(d6nResponse.getSegmentName(), 0.0);
                            d6nResponse.setVariant("null");
                            d6nResponse.setPriority(0.0);
                            d6nResponse.setTotalCount(0);
                            return d6nResponse; // default Banner
                        } else {
                            storedValue += defaultVal;
                            VARIABLES_MAP.put(d6nResponse.getSegmentName(), storedValue);
                        }
                    } else if (percentage <= 50 && 0.0 < percentage) {
                        double actualVal = percentage / (100 - percentage);
                        if (storedValue >= 1) {
                            VARIABLES_MAP.put(d6nResponse.getSegmentName(), 0.0);
                            return d6nResponse; // actual Banner
                        } else {
                            storedValue += actualVal;
                            VARIABLES_MAP.put(d6nResponse.getSegmentName(), storedValue);
                            d6nResponse.setVariant("null");
                            d6nResponse.setPriority(0.0);
                            d6nResponse.setTotalCount(0);
                            return d6nResponse;
                        }
                    }
                }
        }
        }catch (Exception e){
            return d6nResponse;
        }
        //Without AB Test
        return d6nResponse;
    }
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
// Define the A/B testing parameters
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