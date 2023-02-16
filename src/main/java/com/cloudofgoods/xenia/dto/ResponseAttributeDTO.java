package com.cloudofgoods.xenia.dto;

import lombok.Data;

@Data
public class ResponseAttributeDTO {
private String label;
private String javaFormat;

    public ResponseAttributeDTO(String label, String javaFormat) {
        this.label = label;
        this.javaFormat = javaFormat;
    }
}
