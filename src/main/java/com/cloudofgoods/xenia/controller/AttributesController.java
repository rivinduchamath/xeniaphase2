package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.AttributesService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/d6n/attributes")
@RequiredArgsConstructor
public class AttributesController {

    private final AttributesService attributesService;

    @PostMapping(value = "${server.servlet.saveAttributes}")
    @Transactional
    @Description("Add AttributesObject")
    public ServiceResponseDTO saveAttributes(@RequestBody AttributeRequestDTO attributesDTO) {
        log.info ("LOG::Inside the AttributesController saveAttributes ");
        return attributesService.saveAttribute (attributesDTO);
    }

    @PostMapping(value = "${server.servlet.getAttributes}")
    @Transactional
    @Description("Get AttributesObject")
    public ServiceGetResponseDTO getAttributes(@RequestBody GetRequestAttributeDTO paginationDTO) {
        log.info ("LOG::Inside the AttributesController getAttributes");
        return attributesService.getAttribute (paginationDTO.getPage (), paginationDTO.getSize (), paginationDTO.getOrganization (), paginationDTO.getAttributeName (), paginationDTO.getType ());
    }

    @DeleteMapping(value = "${server.servlet.deleteAttribute}")
    @Transactional
    @Description("Delete AttributesObject")
    public ServiceResponseDTO deleteAttribute(@RequestParam String attributeUuid, @RequestParam String organizationUuid) {
        log.info ("LOG::Inside the AttributesController deleteAttribute ");
        return attributesService.deleteAttribute (attributeUuid,organizationUuid);
    }
}
