package com.cloudofgoods.xenia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardAnalyticsRequestDTO {
    private String organizationId;
    private int page;
    private int size;
}
