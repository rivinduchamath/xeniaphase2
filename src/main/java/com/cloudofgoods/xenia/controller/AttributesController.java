package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.request.AttributeGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.AttributesService;
import jdk.jfr.Description;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/d6n/attributes")
@RequiredArgsConstructor
@Validated
public class AttributesController {

    private final AttributesService attributesService;

    @PostMapping(value = "${server.servlet.saveAttributes}")
    @Transactional
    @Description("Save Attributes")
    public ServiceResponseDTO saveAttributes(@Valid @RequestBody AttributeRequestDTO attributesDTO) {
        log.info("LOG::Inside the AttributesController saveAttributes ");
        return attributesService.saveAttribute(attributesDTO);
    }

    @PostMapping(value = "${server.servlet.getAttributes}")
    @Transactional
    @Description("Get Attributes")
    public ServiceGetResponseDTO getAttributes(@Valid @RequestBody GetRequestAttributeDTO paginationDTO) {
        log.info("LOG::Inside the AttributesController getAttributes");
        return attributesService.getAttribute(paginationDTO);
    }

    @DeleteMapping(value = "${server.servlet.activeInactiveAttribute}")
    @Transactional
    @Description("activeInactiveAttribute")
    public ServiceResponseDTO activeInactiveAttribute(@RequestParam @NonNull String attributeUuid, @RequestParam @NonNull String organizationUuid, @RequestParam boolean status) {
        log.info("LOG::Inside the AttributesController activeInactiveAttribute ");
        return attributesService.activeInactiveAttribute(attributeUuid, organizationUuid,status);
    }
    @PostMapping(value = "${server.servlet.getSingleAttribute}")
    @Transactional
    @Description("Get Single Attributes")
    public ServiceGetResponseDTO getSingleAttribute(@RequestBody @Valid AttributeGetSingleDTO attributeRequestDTO) {
        log.info("LOG::Inside the AttributesController getSingleAttribute");
        return attributesService.getSingleAttribute(attributeRequestDTO);
    }
}
