package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.request.AttributeGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.TagsDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestTagsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.TagsService;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/d6n/tags")
@Slf4j
@AllArgsConstructor
@Validated
public class TagsController {

    private final TagsService tagsService;

    @PostMapping(value = "${server.servlet.saveTags}")
    @Description("Add Tags")
    @Transactional
    public ServiceResponseDTO saveTags(@Valid @RequestBody TagsDTO tagsDTO) {
        log.info ("LOG::Inside the TagsController saveTags ");
        return tagsService.saveOrUpdateTags (tagsDTO);
    }

    @PostMapping(value = "${server.servlet.getTags}")
    @Transactional
    @Description("Get Tags Object")
    public ServiceGetResponseDTO getTags(@RequestBody @Valid  GetRequestTagsDTO getRequestTagsDTO) {
        log.info ("LOG::Inside the TagsController getTags");
        return tagsService.getTags (getRequestTagsDTO);
    }
    @DeleteMapping(value = "${server.servlet.activeInactiveTags}")
    @Transactional
    @Description("activeInactiveTags")
    public ServiceResponseDTO activeInactiveTags(@RequestParam @NonNull String tagsName, @RequestParam @NonNull String organizationUuid, @RequestParam boolean status) {
        log.info("LOG::Inside the TagsController activeInactiveTags ");
        return tagsService.activeInactiveTags(tagsName, organizationUuid,status);
    }
    @PostMapping(value = "${server.servlet.getSingleTag}")
    @Transactional
    @Description("Get Single Tag")
    public ServiceGetResponseDTO getSingleTag(@RequestBody @Valid TagsDTO tagsDTO) {
        log.info("LOG::Inside the TagsController getSingleAttribute");
        return tagsService.getSingleTags(tagsDTO);
    }
}
