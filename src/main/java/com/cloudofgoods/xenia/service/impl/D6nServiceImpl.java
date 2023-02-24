package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.D6nResponseModelDTO;
import com.cloudofgoods.xenia.dto.caution.MetaData;
import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.UserAnalyticsEntity;
import com.cloudofgoods.xenia.repository.redis.ResponseUserRepository;
import com.cloudofgoods.xenia.service.D6nService;
import com.cloudofgoods.xenia.util.Utility;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class D6nServiceImpl implements D6nService {
    @Autowired
    private DroolServiceImpl droolService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ResponseUserRepository responseUserRepository;

    @Override
    public ServiceResponseDTO makeDecision(int numberOfResponseFrom, int numberOfResponse, String userEmail, User user, List<String> channel, List<String> slot, String organization) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG :: D6nServiceImpl makeDecision() Set Meta Data");
        MetaData metaData = new MetaData();
        metaData.setEndDate(Utility.today);
        metaData.setStartDate(Utility.today);
        slot.replaceAll(String::toUpperCase);
        metaData.setContextId(slot);
//        channel.replaceAll(String::toUpperCase);
        metaData.setChannels(channel);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss.SSSZ");
            Date date = new Date();
            NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
            UUID firstUUID = timeBasedGenerator.generate();
            D6nResponseModelDTO d6nResponseModelDTO = droolService.makeDecision(numberOfResponseFrom, numberOfResponse, metaData, user, organization.toUpperCase(), (dateFormat.format(date) + "##$$##" + firstUUID));
            CompletableFuture.runAsync(() -> databaseAnalytics(d6nResponseModelDTO, userEmail, organization));
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

    private void databaseAnalytics(D6nResponseModelDTO d6nResponseModelDTO, String userEmail, String organization) {
        UserAnalyticsEntity userAnalyticsEntity = new UserAnalyticsEntity();
        userAnalyticsEntity.setUserEmail(userEmail);
        userAnalyticsEntity.setOrganizationName(organization);

        userAnalyticsEntity.setMatchedRulesCount(d6nResponseModelDTO.getSatisfiedConditions().size());
        userAnalyticsEntity.setSatisfiedConditionsName(d6nResponseModelDTO.getSatisfiedConditions());
//        String createdAt = mongoTemplate.indexOps(UserAnalyticsEntity.class).ensureIndex(new Index().on("satisfiedConditionsName", Sort.Direction.ASC).expire(10));
        UserAnalyticsEntity save = responseUserRepository.save(userAnalyticsEntity);

    }

}