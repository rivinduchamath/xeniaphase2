package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.AttributeTableDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.AttributesTableService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/d6n/attribute")
@RequiredArgsConstructor
public class AttributeTableController {

    private final AttributesTableService attributesTableService;

    @PostMapping(value = "${server.servlet.saveOrUpdateAttributeTable}")
    @Transactional
    @Description("Add Attributes Table Object")
    public ServiceResponseDTO saveOrUpdateAttributeTable(@RequestBody AttributeTableDTO attributeTableDTO) {
        log.info("LOG::Inside the AttributeTableController saveAttributesTable ");
        return attributesTableService.saveOrUpdateAttributeTable(attributeTableDTO);
    }

    @PostMapping(value = "${server.servlet.getAttributesTable}")
    @Transactional
    @Description("Get Attributes Table Object")
    public ServiceResponseDTO getAttributesTable(@RequestBody AttributeTableRequestDTO attributeTableDTO) {
        log.info("LOG::Inside the AttributeTableController getAttributesTable ");
        return attributesTableService.getAttributes(attributeTableDTO);
    }
}
