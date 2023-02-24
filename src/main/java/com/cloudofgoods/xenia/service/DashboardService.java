package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.request.DashboardAnalyticsRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceOrganizationResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface DashboardService {
    ServiceResponseDTO campaignPerformance(DashboardAnalyticsRequestDTO dashboardAnalyticsRequestDTO);

    ServiceOrganizationResponseDTO organizationDataDashboard(DashboardAnalyticsRequestDTO dashboardAnalyticsRequestDTO);
}
