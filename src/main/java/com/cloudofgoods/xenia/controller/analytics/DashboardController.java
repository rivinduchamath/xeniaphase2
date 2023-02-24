package com.cloudofgoods.xenia.controller.analytics;

import com.cloudofgoods.xenia.dto.request.DashboardAnalyticsRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceOrganizationResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.DashboardService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Slf4j
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping(value = "${server.servlet.organizationDataDashboard}")
    @Transactional
    @Description("Under Organization All Campaigns, Active Campaigns and Scheduled Campaigns, Expired Within Week Before," +
            " Get Requests To the Organization and Success ")
    public ServiceOrganizationResponseDTO organizationDataDashboard(@RequestBody DashboardAnalyticsRequestDTO dashboardAnalyticsRequestDTO) {
        log.info("LOG::Inside the DashboardController organizationDataDashboard ");
        return dashboardService.organizationDataDashboard(dashboardAnalyticsRequestDTO);
    }


    @PostMapping(value = "${server.servlet.campaignPerformance}")
    @Transactional
    @Description("Under Campaign total Rules Under Campaign, Get Requests To the  Campaigns and Success With Campaign Details")
    public ServiceResponseDTO campaignPerformance(@RequestBody DashboardAnalyticsRequestDTO dashboardAnalyticsRequestDTO) {
        log.info("LOG::Inside the DashboardController campaignPerformance ");
        return dashboardService.campaignPerformance(dashboardAnalyticsRequestDTO);
    }
}
