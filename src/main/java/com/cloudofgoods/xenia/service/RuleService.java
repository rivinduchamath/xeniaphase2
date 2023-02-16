package com.cloudofgoods.xenia.service;


import com.cloudofgoods.xenia.dto.RuleRequestRootDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RuleService {

    ServiceResponseDTO findByID(String id);

    ServiceResponseDTO removeRuleFromKBAndDatabase(String ruleId) throws ExecutionException, InterruptedException;

    RuleRequestRootEntity saveOrUpdateSingleRule(RuleRequestRootDTO ruleRequestRootModel) throws ExecutionException, InterruptedException;

    ServiceResponseDTO updateCampaignStatus(String campaignId, String status) throws ExecutionException, InterruptedException;

//    ServiceResponseDTO saveOrUpdateRule(RuleRequestRootDTO ruleRootModel);

    ServiceResponseDTO updateRules(List<String> ruleRootModel);


}
