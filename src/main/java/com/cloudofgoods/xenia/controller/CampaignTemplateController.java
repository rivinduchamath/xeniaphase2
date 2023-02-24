package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.CampaignTemplateDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.CampaignTemplateService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/d6n/campaign")
@RequiredArgsConstructor
public class CampaignTemplateController {
    private final CampaignTemplateService templateService;

    @PostMapping(value = "${server.servlet.templateSave}")
    @Description("If Send Id It Will Update Template, Otherwise Save as a new Template ")
    public ServiceResponseDTO saveTemplate(@RequestBody CampaignTemplateDTO ruleRootModelDTO) {
        log.info (ruleRootModelDTO + " LOG::Inside the CampaignTemplateController saveTemplate ");
        log.info ("LOG::Inside the CampaignTemplateController saveTemplate Inside try");
        return templateService.saveOrUpdateTemplate(ruleRootModelDTO);
    }
    @GetMapping(value = "${server.servlet.getPagination}")
    public ServiceResponseDTO getAllTemplateWithPagination(@RequestParam int page, @RequestParam int size) {
        log.info ("LOG:: CampaignTemplateController getAllTemplatePagination");
        return templateService.getAllCampTemplatePagination (page, size);
    }
    @DeleteMapping(value = "${server.servlet.deleteTemplate}")
    @Transactional
    @Description("Delete AttributesObject")
    public ServiceResponseDTO deleteTemplate(@RequestParam String templateId) {
        log.info ("LOG::Inside the CampaignTemplateController deleteTemplate ");
        return templateService.deleteTemplate (templateId);
    }
}
