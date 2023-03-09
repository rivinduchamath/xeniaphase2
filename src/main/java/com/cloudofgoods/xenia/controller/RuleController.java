package com.cloudofgoods.xenia.controller;


import com.cloudofgoods.xenia.dto.RuleRequestRootDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import com.cloudofgoods.xenia.service.RuleService;
import com.cloudofgoods.xenia.util.RuleStatus;
import jdk.jfr.Description;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/d6n/segment")
@Slf4j
@Validated
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @Transactional
    @PostMapping(value = "${server.servlet.saveRule}")
    @Description("If Send Id It Will Update SegmentsObject, Otherwise Save as a new rule ")
    public RuleRequestRootEntity saveRule(@RequestBody @Valid RuleRequestRootDTO ruleRootModel) throws ExecutionException, InterruptedException {
        log.info("LOG::Inside the RuleController saveRule ");
        return ruleService.saveOrUpdateSingleRule(ruleRootModel);
    }

    @Transactional
    @PostMapping(value = "${server.servlet.updateRule}")
    @Description("If Send Id It Will Update SegmentsObject, Otherwise Save as a new rule ")
    public ServiceResponseDTO saveOrUpdateRules(@RequestBody List<String> ruleRootModel) {
        log.info("LOG::Inside the RuleController saveOrUpdateRuleListRules ");
        return ruleService.updateRules(ruleRootModel);
    }

    @PostMapping(value = "${server.servlet.updateCampaignStatus}")
    @Description("Update Campaign Status ")
    public ServiceResponseDTO updateCampaignStatus(@RequestParam @NonNull String campaignId, @RequestParam String status) throws ExecutionException, InterruptedException {
        if (Objects.equals(status, RuleStatus.INACTIVE.name())
                || Objects.equals(status, RuleStatus.ACTIVE.name())) {
            log.info("LOG::Inside the RuleController updateCampaignStatus " + campaignId);
            return ruleService.updateCampaignStatus(campaignId, status);
        } else {
            ServiceResponseDTO responseDTO = new ServiceResponseDTO();
            responseDTO.setMessage("Success");
            responseDTO.setCode("4000");
            responseDTO.setDescription("REQUEST NOT SATISFIABLE");
            responseDTO.setHttpStatus("OK");
            return responseDTO;
        }
    }

    @DeleteMapping(value = "${server.servlet.deleteCampaignWithAllSegments}")
    @Description("Delete Campaign With All Segments From Id If Delete It Will Delete With All Rules(Multiple Rules)")
    public ServiceResponseDTO deleteCampaignWithAllSegments(@RequestParam @NonNull String campaignId) throws ExecutionException, InterruptedException {
        log.info("LOG::Inside the RuleController deleteCampaignWithAllSegments " + campaignId);
        return ruleService.removeRuleFromKBAndDatabase(campaignId);
    }

    @GetMapping(value = "${server.servlet.getCampaignById}/{id}")
    public ServiceResponseDTO getCampaignById(@PathVariable("id") @NonNull String id) {
        log.info("LOG::Inside the RuleController getRuleById ");
        return ruleService.findByID(id);
    }
}