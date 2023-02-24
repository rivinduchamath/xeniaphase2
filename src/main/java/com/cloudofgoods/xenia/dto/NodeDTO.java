package com.cloudofgoods.xenia.dto;

import com.cloudofgoods.xenia.models.FactObject;
import com.cloudofgoods.xenia.util.Conjunction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeDTO {
    public Conjunction conj;
    public ArrayList <FactObject> children;
}
