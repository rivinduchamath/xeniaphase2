package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.TemplateEntity;
import com.cloudofgoods.xenia.service.TemplateService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/d6n/template")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping(value = "${server.servlet.saveTemplate}")
    @Description("If Send Id It Will Update Template, Otherwise Save as a new Template ")
    public ServiceResponseDTO saveTemplate(@RequestBody TemplateEntity ruleRootModel) {
        log.info (ruleRootModel + " LOG::Inside the TemplateController saveTemplate");
        log.info ("LOG::Inside the TemplateController saveTemplate Inside try");
        return templateService.saveTemplate (ruleRootModel);
    }

    @GetMapping(value = "${server.servlet.getAllTemplateWithPagination}")
    public ServiceResponseDTO getAllTemplateWithPagination(@RequestParam int page, @RequestParam int size) {
        if (!(page < 0 && size <= 0)) {
            return templateService.getAllTemplatePagination (page, size);
        }else {
            log.info ("LOG:: TemplateController getAllTemplatePagination Error With Request Param");
            ServiceResponseDTO responseDTO = new ServiceResponseDTO ();
            responseDTO.setMessage ("Success");
            responseDTO.setCode ("4000");
            responseDTO.setDescription ("Bad Request");
            responseDTO.setHttpStatus ("OK");
            return responseDTO;
        }
    }

    @GetMapping(value = "${server.servlet.getAllTemplates}")
    @Description("getAllTemplates")
    public ServiceResponseDTO getAllTemplates() {
        log.info ("LOG:: TemplateController getAllTemplates");
        return templateService.getAllTemplate ();
    }

    @GetMapping(value = "${server.servlet.getTemplateByName}/{name}")
    @Description("getTemplateByName")
    public ServiceResponseDTO getTemplateByName(@PathVariable("name") String name) {
        log.info ("LOG:: TemplateController getTemplateByName");
        return templateService.getTemplateByName (name);
    }

    @GetMapping(value = "${server.servlet.getTemplateById}/{templateId}")
    public ServiceResponseDTO getTemplateById(@PathVariable("templateId") String templateId) {
        log.info ("LOG:: TemplateController getTemplateById");
        return templateService.getTemplateById (templateId);
    }

    @DeleteMapping(value = "${server.servlet.deleteTemplateById}/{templateId}")
    public ServiceResponseDTO deleteTemplateById(@PathVariable("templateId") String templateId) throws ExecutionException, InterruptedException {
        log.info ("LOG:: TemplateController deleteTemplateById");
        return templateService.deleteTemplateById (templateId);
    }
}