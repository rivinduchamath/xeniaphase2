package com.cloudofgoods.xenia.models;

import lombok.Data;

import java.util.List;

@Data
public class CustomAttributeObject {
    private List <AttributesObject> attributes;
    private long size;
}
