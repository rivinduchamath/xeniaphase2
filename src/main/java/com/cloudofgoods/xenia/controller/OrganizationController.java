package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.OrganizationDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.OrganizationService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/d6n/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping(value = "${server.servlet.saveOrganization}")
    @Description("Add OrganizationEntity ")
    @Transactional
    public ServiceResponseDTO saveOrganization(@RequestBody OrganizationDTO organizationDTO) {
        log.info ("LOG::Inside the OrganizationController saveOrganization ");
        return organizationService.saveOrUpdateOrganization (organizationDTO);
    }
    @GetMapping(value = "${server.servlet.getOrganization}")
    @Description("Get OrganizationEntity ")
    @Transactional
    public ServiceResponseDTO getOrganizations() {
        log.info ("LOG::Inside the OrganizationController saveOrganization ");
        return organizationService.getOrganization ();
    }
}
