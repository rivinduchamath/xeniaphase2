package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class D6nDTO {

    private int numberOfResponseFrom;
    private int numberOfResponse;
    private String userEmail;
    private List<String> channels;
    private List<String> slot;
    @NotEmptyOrNull(message = "Organization Uuid Must Not Be Empty")
    private String organization;
    private LinkedHashMap<String, Object> userData; // @TODO ADd
}
