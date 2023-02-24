package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.util.Conjunction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FactObject {
    public String factName;
    public String operator;

    public String value;
    private String type;
    public Conjunction conj;
    public ArrayList<FactObject> children;

}
