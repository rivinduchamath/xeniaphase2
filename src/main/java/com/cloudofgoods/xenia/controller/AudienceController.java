package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.AudienceDTO;
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

    @GetMapping(value = "${server.servlet.getAudience}")
    @Description("Get OrganizationEntity")
    @Transactional
    public ServiceResponseDTO getAudience(@RequestParam @NonNull String audienceId) {
        log.info("LOG::Inside the AudienceController getAudience ");
        return audienceService.getAudienceById(audienceId);
    }

    @PostMapping(value = "${server.servlet.getAudienceWithPagination}")
    public ServiceResponseDTO getAudienceWithPagination(@RequestBody @Valid AudienceRequestDTO audienceRequestDTO) {
        log.info("LOG:: AudienceController getAudienceWithPagination ");
        return audienceService.getAudienceWithPagination(audienceRequestDTO.getOrganizationId(), audienceRequestDTO.getPage(), audienceRequestDTO.getSize());
    }
}
