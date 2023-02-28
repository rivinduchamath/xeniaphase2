package com.cloudofgoods.xenia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ServiceGetResponseDTO implements Serializable {
    Object data;
    long count;
    Object error;
    Object message;
    String code;
    String httpStatus;
    String description;
}