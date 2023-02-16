package com.cloudofgoods.xenia.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisCommonResponseDTO {

    private String allCount;
    private String organization;
    private String successCount;
    private String dateRange;
    private Object error;
    private Object message;
    private String code;
    private String httpStatus;
    private String description;

}
