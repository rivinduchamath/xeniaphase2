package com.cloudofgoods.xenia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceGetResponseDTO {
    Object data;
    long count;
    Object error;
    Object message;
    String code;
    String httpStatus;
    String description;
}