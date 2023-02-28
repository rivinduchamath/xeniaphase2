package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.AudienceDTO;
import com.cloudofgoods.xenia.dto.request.AudienceGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.AudienceRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.AudienceService;
import jdk.jfr.Description;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/d6n/audience")
@RequiredArgsConstructor
@Validated
public class AudienceController {

    private final AudienceService audienceService;

    @PostMapping(value = "${server.servlet.saveAudience}")
    @Description("Add Audience")
    @Transactional
    public ServiceResponseDTO saveAudience(@Valid @RequestBody AudienceDTO audienceDTO) {
        log.info("LOG::Inside the AudienceController saveAudience ");
        return audienceService.saveAudience(audienceDTO);
    }

    @PostMapping(value = "${server.servlet.getAudience}")
    @Description("Get OrganizationEntity")
    @Transactional
    public ServiceResponseDTO getAudience(@RequestBody AudienceGetSingleDTO audienceGetSingleDTO) {
        log.info("LOG::Inside the AudienceController getAudience ");
        return audienceService.getAudienceById(audienceGetSingleDTO);
    }

    @PostMapping(value = "${server.servlet.getAudienceWithPagination}")
    public ServiceResponseDTO getAudienceWithPagination(@RequestBody @Valid AudienceRequestDTO audienceRequestDTO) {
        log.info("LOG:: AudienceController getAudienceWithPagination ");
        return audienceService.getAudienceWithPagination(audienceRequestDTO.getOrganizationId(), audienceRequestDTO.getPage(), audienceRequestDTO.getSize());
    }

    @DeleteMapping(value = "${server.servlet.activeInactiveAudience}")
    @Transactional
    @Description("activeInactiveAudience")
    public ServiceResponseDTO activeInactiveAudience(@RequestParam @NonNull String audienceUuid, @RequestParam @NonNull String organizationUuid, @RequestParam boolean status) {
        log.info("LOG::Inside the AudienceController activeInactiveAudience ");
        return audienceService.activeInactiveAudience(audienceUuid, organizationUuid,status);
    }
}
