package com.cloudofgoods.xenia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDTO implements Serializable {
    Object data;
    Object error;
    Object message;
    String code;
    String httpStatus;
    String description;
}
