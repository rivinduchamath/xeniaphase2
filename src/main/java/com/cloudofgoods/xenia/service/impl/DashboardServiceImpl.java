package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.request.DashboardAnalyticsRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceOrganizationResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import com.cloudofgoods.xenia.models.AttributesObject;
import com.cloudofgoods.xenia.models.CampaignsObjects;
import com.cloudofgoods.xenia.repository.RootRuleRepository;
import com.cloudofgoods.xenia.service.DashboardService;
import com.cloudofgoods.xenia.util.RuleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    private final RootRuleRepository rootRuleRepository;

    @Override
    public ServiceResponseDTO campaignPerformance(DashboardAnalyticsRequestDTO dashboardAnalyticsRequestDTO) {
        return null;
    }

    @Override
//    Under Organization
//    All Campaigns(PageRequest), All Campaigns Count,
//    Active Campaigns(PageRequest), All Active Campaigns Count,
//    Scheduled Campaigns (PageRequest), All Scheduled Campaigns Count,
//    All Expired Within Week Before Count,  Expired Within Week Before (PageRequest)
//    Get Requests To the Organization count and Success count
    public ServiceOrganizationResponseDTO organizationDataDashboard(DashboardAnalyticsRequestDTO analyticsRequestDTO) {

        ServiceOrganizationResponseDTO serviceResponseDTO = new ServiceOrganizationResponseDTO();

        int totalCampaignsUnderOrganization = 0;
        int totalActiveCampaigns = 0;
        int totalSheduledCampaignsObjectsList = 0;
        int totalExpiredWeekBefore = 0;
        List<RuleRequestRootEntity> organizationEntity = rootRuleRepository.findByOrganizationIdEquals(analyticsRequestDTO.getOrganizationId(), PageRequest.of(analyticsRequestDTO.getPage(), analyticsRequestDTO.getSize()));
        if (organizationEntity != null) {
            List<CampaignsObjects> allCampaignsObjectsList = new ArrayList<>();
            List<CampaignsObjects> activeCampaignsObjectsList = new ArrayList<>();
            List<CampaignsObjects> sheduledCampaignsObjectsList = new ArrayList<>();
            List<CampaignsObjects> totalExpiredWeekBeforeList = new ArrayList<>();
            organizationEntity.forEach(ruleRequestRoot -> {
                CampaignsObjects campaignsObject = new CampaignsObjects();
                campaignsObject.setCampaignName(ruleRequestRoot.getCampaignName());
                campaignsObject.setCampaignDescription(ruleRequestRoot.getCampaignDescription());
                campaignsObject.setCampaignId(ruleRequestRoot.getCampaignId());
                campaignsObject.setEndDate(String.valueOf(ruleRequestRoot.getEndDateTime()));
                campaignsObject.setCreatedDate(String.valueOf(ruleRequestRoot.getCreatedDate()));
                campaignsObject.setStatus(ruleRequestRoot.getStatusEnum());
                allCampaignsObjectsList.add(campaignsObject);
                if (ruleRequestRoot.getStatusEnum().equals(RuleStatus.ACTIVE)) {
                    activeCampaignsObjectsList.add(campaignsObject);
                }
                Date currentDate = new Date();
                if (ruleRequestRoot.getStartDateTime().after(currentDate)) {
                    sheduledCampaignsObjectsList.add(campaignsObject);
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                Date oneWeekAgo = calendar.getTime();
                if (ruleRequestRoot.getEndDateTime().after(oneWeekAgo) && ruleRequestRoot.getEndDateTime().before(currentDate)) {
                    totalExpiredWeekBeforeList.add(campaignsObject);
                }
            });
            totalCampaignsUnderOrganization = allCampaignsObjectsList.size();
            totalActiveCampaigns = activeCampaignsObjectsList.size();
            totalSheduledCampaignsObjectsList = sheduledCampaignsObjectsList.size();
            totalExpiredWeekBefore = totalExpiredWeekBeforeList.size();
            int pageStart = Math.min(analyticsRequestDTO.getPage(), analyticsRequestDTO.getSize());
            int pageEnd = Math.min(analyticsRequestDTO.getPage(), analyticsRequestDTO.getSize());

            List<CampaignsObjects> pageContent = allCampaignsObjectsList.subList(pageStart, pageEnd);
            Pageable pageable = PageRequest.of(pageStart, pageContent.size());
            Page<CampaignsObjects> pageAll = new PageImpl<>(pageContent, pageable, totalCampaignsUnderOrganization);
            Page<CampaignsObjects> pageActive = new PageImpl<>(pageContent, pageable, totalActiveCampaigns);
            Page<CampaignsObjects> pageScheduled = new PageImpl<>(pageContent, pageable, totalSheduledCampaignsObjectsList);
            Page<CampaignsObjects> pageWeek = new PageImpl<>(pageContent, pageable, totalExpiredWeekBefore);

            serviceResponseDTO.setAllCampaignsData(pageAll.getContent());
            serviceResponseDTO.setAllCampaignsDataCount(totalCampaignsUnderOrganization);
            serviceResponseDTO.setActiveCampaignsData(pageActive.getContent());
            serviceResponseDTO.setActiveCampaignsDataCount(totalActiveCampaigns);
            serviceResponseDTO.setScheduledCampaignsData(pageScheduled.getContent());
            serviceResponseDTO.setScheduledCampaignsDataCount(totalSheduledCampaignsObjectsList);
            serviceResponseDTO.setExpiredCampaignsData(pageWeek);
            serviceResponseDTO.setExpiredCampaignsDataCount(totalExpiredWeekBefore);
            return serviceResponseDTO;
        }
        return null;
    }
}
