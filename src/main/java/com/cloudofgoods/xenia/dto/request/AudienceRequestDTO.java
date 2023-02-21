package com.cloudofgoods.xenia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudienceRequestDTO {
    private String organizationId;
    private int page;
    private int size;
}
