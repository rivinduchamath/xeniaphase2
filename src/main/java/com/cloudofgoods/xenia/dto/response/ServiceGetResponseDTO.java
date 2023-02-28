package com.cloudofgoods.xenia.dto.response;

import io.swagger.annotations.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Component
//@Scope(name = "prototype", description = "")
public class ServiceGetResponseDTO implements Serializable {
    Object data;
    long count;
    Object error;
    Object message;
    String code;
    String httpStatus;
    String description;
}