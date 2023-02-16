package com.cloudofgoods.xenia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRequestAttributeDTO {

    private int page;
    private int size;
    private String organization;
    private String attributeName;
    private List<String> type;

}
