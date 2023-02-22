package com.cloudofgoods.xenia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class D6nDTO {


   private int numberOfResponseFrom;
    private int numberOfResponse;
    private String userEmail;
    private List<String> channels;
    private  List<String> slot;
    private String organization;
}
