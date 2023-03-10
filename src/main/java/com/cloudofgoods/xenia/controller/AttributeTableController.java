package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.AttributeTableDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AttributeTableRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.AttributesTableService;
import jdk.jfr.Description;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/d6n/attribute/table")
@RequiredArgsConstructor
@Validated
public class AttributeTableController {

    private final AttributesTableService attributesTableService;

    @PostMapping(value = "${server.servlet.saveOrUpdateAttributeTable}")// Avg Response Time 15ms Local
    @Description("Add Attributes Table Object")
    public ServiceResponseDTO saveOrUpdateAttributeTable(@RequestBody @Valid AttributeTableDTO attributeTableDTO) {
        log.info("LOG::Inside the AttributeTableController saveAttributesTable ");
        return attributesTableService.saveOrUpdateAttributeTable(attributeTableDTO);
    }

    @PostMapping(value = "${server.servlet.getAttributesTable}")// Avg Response Time 15ms Local
    @Description("Get Attributes Table Object")
    public ServiceGetResponseDTO getAttributesTable(@RequestBody @Valid AttributeTableRequestDTO attributeTableDTO) {
        log.info("LOG::Inside the AttributeTableController getAttributesTable ");
        return attributesTableService.getAttributesTables(attributeTableDTO);
    }
    @DeleteMapping(value = "${server.servlet.activeInactiveAttributeTable}")
    @Description("activeInactiveAttribute")// Avg Response Time 15ms Local
    public ServiceResponseDTO activeInactiveAttributeTable(@RequestParam @NonNull String attributeTableName, @RequestParam @NonNull String organizationUuid, @RequestParam boolean status) {
        log.info("LOG::Inside the AttributesController activeInactiveAttribute ");
        return attributesTableService.activeInactiveAttributeTable(attributeTableName, organizationUuid,status);
    }
    @PostMapping(value = "${server.servlet.getSingleAttributeTable}")
    @Description("Get Single Get Single AttributeTable")
    public ServiceGetResponseDTO getSingleAttributeTable(@RequestBody @Valid AttributeTableGetSingleDTO attributeRequestDTO) {
        log.info("LOG::Inside the AttributesController getSingleAttributeTable");
        return attributesTableService.getSingleAttributeTable(attributeRequestDTO);
    }
}
