package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.util.Conjunction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeObject {
    public Conjunction conj;
    public ArrayList<FactObject> children;
}
