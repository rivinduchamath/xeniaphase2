package com.cloudofgoods.xenia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Component
public class ServiceResponseDTO implements Serializable {
    Object data;
    Object error;
    Object message;
    String code;
    String httpStatus;
    String description;

    public ServiceResponseDTO(String code, String httpStatus, Object message, String description, Object data, Object error) {
        this.data = data;
        this.error = error;
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }


}
