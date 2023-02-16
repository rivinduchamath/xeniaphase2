package com.cloudofgoods.xenia.dto.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisOrganizationRequestDTO {
    private String organization;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/ddHH:mm:ss.SSSZ", timezone = "UTC")
    private LocalDateTime startsDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/ddHH:mm:ss.SSSZ", timezone = "UTC")
    private LocalDateTime endDate;

}
