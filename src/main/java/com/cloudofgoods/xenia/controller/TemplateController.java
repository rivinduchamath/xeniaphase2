package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.SegmentTemplateEntity;
import com.cloudofgoods.xenia.service.TemplateService;
import jdk.jfr.Description;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/d6n/template")
@Validated
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping(value = "${server.servlet.saveTemplate}")
    @Description("If Send Id It Will Update Template, Otherwise Save as a new Template ")
    public ServiceResponseDTO saveTemplate(@RequestBody SegmentTemplateEntity ruleRootModel) {
        log.info("LOG::Inside the TemplateController saveTemplate");
        return templateService.saveTemplate(ruleRootModel);
    }

    @GetMapping(value = "${server.servlet.getAllTemplateWithPagination}")
    public ServiceResponseDTO getAllTemplateWithPagination(@RequestParam @NonNegative int page, @RequestParam @NonNegative int size) {
        log.info("LOG::Inside the TemplateController getAllTemplateWithPagination");
        return templateService.getAllTemplatePagination(page, size);
    }

    @GetMapping(value = "${server.servlet.getAllTemplates}")
    @Description("getAllTemplates")
    public ServiceResponseDTO getAllTemplates() {
        log.info("LOG:: TemplateController getAllTemplates");
        return templateService.getAllTemplate();
    }

    @GetMapping(value = "${server.servlet.getTemplateByName}/{name}")
    @Description("getTemplateByName")
    public ServiceResponseDTO getTemplateByName(@PathVariable("name") @NonNull String name) {
        log.info("LOG:: TemplateController getTemplateByName");
        return templateService.getTemplateByName(name);
    }

    @GetMapping(value = "${server.servlet.getTemplateById}/{templateId}")
    public ServiceResponseDTO getTemplateById(@PathVariable("templateId") @NonNull String templateId) {
        log.info("LOG:: TemplateController getTemplateById");
        return templateService.getTemplateById(templateId);
    }

    @DeleteMapping(value = "${server.servlet.deleteTemplateById}")
    public ServiceResponseDTO deleteTemplateById(@RequestParam("templateId") @NonNull String templateId) throws ExecutionException, InterruptedException {
        log.info("LOG:: TemplateController deleteTemplateById");
        return templateService.deleteTemplateById(templateId);
    }
}