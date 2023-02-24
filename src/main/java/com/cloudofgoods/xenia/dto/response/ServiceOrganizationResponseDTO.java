package com.cloudofgoods.xenia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrganizationResponseDTO implements Serializable {
    Object allCampaignsData;
    long allCampaignsDataCount;
    Object activeCampaignsData;
    long activeCampaignsDataCount;
    Object scheduledCampaignsData;
    long scheduledCampaignsDataCount;
    Object expiredCampaignsData;
    long expiredCampaignsDataCount;
    long allRequestCount;
    long successRequestCount;


    Object error;
    Object message;
    String code;
    String httpStatus;
    String description;
}