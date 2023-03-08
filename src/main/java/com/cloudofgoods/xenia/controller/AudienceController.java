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
    @Description("Add Audience")// Avg Response Time 15ms Local
    public ServiceResponseDTO saveAudience(@RequestBody @Valid AudienceDTO audienceDTO) {
        log.info("LOG::Inside the AudienceController saveAudience ");
        return audienceService.saveAudience(audienceDTO);
    }

    @PostMapping(value = "${server.servlet.getAudience}")
    @Description("Get OrganizationEntity") // Avg Response Time 15ms Local
    public ServiceResponseDTO getAudience(@RequestBody @Valid AudienceGetSingleDTO audienceGetSingleDTO) {
        log.info("LOG::Inside the AudienceController getAudience ");
        return audienceService.getAudienceById(audienceGetSingleDTO);
    }

    @PostMapping(value = "${server.servlet.getAudienceWithPagination}") // Avg Response Time 15ms Local
    public ServiceResponseDTO getAudienceWithPagination(@RequestBody @Valid AudienceRequestDTO audienceRequestDTO) {
        log.info("LOG:: AudienceController getAudienceWithPagination ");
        return audienceService.getAudienceWithPagination(audienceRequestDTO);
    }

    @DeleteMapping(value = "${server.servlet.activeInactiveAudience}")
    @Description("activeInactiveAudience")// Avg Response Time 15ms Local
    public ServiceResponseDTO activeInactiveAudience(@RequestParam @NonNull String audienceUuid, @RequestParam @NonNull String organizationUuid, @RequestParam boolean status) {
        log.info("LOG::Inside the AudienceController activeInactiveAudience ");
        return audienceService.activeInactiveAudience(audienceUuid, organizationUuid,status);
    }
}
