package com.cloudofgoods.xenia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerObject {
    private List <CustomerEntityObject> customerEntityObjects;
    private long total;
}
