package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.AudienceDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.AudienceService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/d6n/audience")
@RequiredArgsConstructor
public class AudienceController {

    private final AudienceService audienceService;


    @PostMapping(value = "${server.servlet.saveAudience}")
    @Description("Add Audience")
    @Transactional
    public ServiceResponseDTO saveAudience(@RequestBody AudienceDTO audienceDTO) {
        log.info("LOG::Inside the AudienceController saveAudience ");
        return audienceService.saveAudience(audienceDTO);
    }
    @GetMapping(value = "${server.servlet.getAudience}")
    @Description("Get OrganizationEntity ")
    @Transactional
    public ServiceResponseDTO getAudience(@RequestParam String audienceId) {
        log.info ("LOG::Inside the AudienceController getAudience ");
        return audienceService.getAudienceById (audienceId);
    }

    @GetMapping(value = "${server.servlet.getAudienceWithPagination}")
    public ServiceResponseDTO getAudienceWithPagination(@RequestParam String organizationId, @RequestParam int page, @RequestParam int size) {
        log.info ("LOG:: InitialPageController getCampaignForInitialPage ");
        if (page >= 0 && size > 0) {
            log.info ("LOG:: InitialPageController getCampaignForInitialPage !(page < 0 && size <= 0)");
            return audienceService.getAudienceWithPagination (organizationId, page, size);
        }else {
            log.info ("LOG:: InitialPageController getCampaignForInitialPage Error With Request Param");
            ServiceResponseDTO responseDTO = new ServiceResponseDTO ();
            responseDTO.setMessage ("Success");
            responseDTO.setCode ("4000");
            responseDTO.setDescription ("REQUESTED RANGE NOT SATISFIABLE");
            responseDTO.setHttpStatus ("OK");
            return responseDTO;
        }
    }
}
