package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.TagsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.AuthUser;
import com.cloudofgoods.xenia.service.TagsService;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@Slf4j
@AllArgsConstructor
public class TagsController {

    private final TagsService tagsService;

    @PostMapping(value = "${server.servlet.saveTags}")
    @Description("Add Tags")
    @Transactional
    public ServiceResponseDTO saveTags(@RequestBody TagsDTO tagsDTO) {
        log.info ("LOG::Inside the CustomerController saveUser ");
        return tagsService.saveOrUpdateTags (tagsDTO);
    }
}
