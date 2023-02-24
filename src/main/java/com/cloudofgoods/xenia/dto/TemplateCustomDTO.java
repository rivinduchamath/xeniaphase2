package com.cloudofgoods.xenia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateCustomDTO {

    private List<TemplateDTO> templateDTO;
    private long total;

}
