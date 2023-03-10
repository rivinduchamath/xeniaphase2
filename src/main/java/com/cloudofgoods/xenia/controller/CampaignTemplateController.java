package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.CampaignTemplateDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.CampaignTemplateService;
import jdk.jfr.Description;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/d6n/campaign")
@RequiredArgsConstructor
@Validated
public class CampaignTemplateController {
    private final CampaignTemplateService templateService; //////////////////// @TODO NOT COMPLETE

    @PostMapping(value = "${server.servlet.templateSave}")
    @Description("If Send Id It Will Update Template, Otherwise Save as a new Template ")
    public ServiceResponseDTO saveTemplate(@RequestBody @Valid CampaignTemplateDTO ruleRootModelDTO) {
        log.info (" LOG::Inside the CampaignTemplateController saveTemplate "+ ruleRootModelDTO.campaignName);
        return templateService.saveOrUpdateTemplate(ruleRootModelDTO);
    }
    @GetMapping(value = "${server.servlet.getPagination}")
    public ServiceGetResponseDTO getAllTemplateWithPagination(@RequestParam @NonNegative int page, @RequestParam @NonNegative int size) {
        log.info ("LOG:: CampaignTemplateController getAllTemplatePagination");
        return templateService.getAllCampTemplatePagination (page, size);
    }
    @DeleteMapping(value = "${server.servlet.deleteTemplate}")
    @Description("Delete AttributesObject")
    public ServiceResponseDTO deleteTemplate(@RequestParam @NonNull String templateId) {
        log.info ("LOG::Inside the CampaignTemplateController deleteTemplate ");
        return templateService.deleteTemplate (templateId);
    }
}
