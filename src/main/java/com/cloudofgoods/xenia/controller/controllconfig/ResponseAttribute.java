package com.cloudofgoods.xenia.controller.controllconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAttribute {

    String label;
    String value;
    String attrType;
    String type;
    List<Object > values = new ArrayList<>();
    List<Object > operators = new ArrayList<>();

}
