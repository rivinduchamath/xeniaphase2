package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.models.FactObject;
import com.cloudofgoods.xenia.util.Conjunction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeDTO {
    public Conjunction conj;
    public ArrayList <FactObject> children;
}
