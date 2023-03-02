package com.cloudofgoods.xenia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class D6nResponseModelDTO implements Serializable {

    private String slotId;
    private String variant;
    private double priority;
    List<String> satisfiedConditions =  new ArrayList<>();
    private double totalCount;

    public void addToResponse(double priority, String slotId, String variant) {
        this.variant = variant;
        this.slotId = slotId;
        this.priority = priority;
        this.satisfiedConditions.add("{ 'priority' : '" + priority + "' ,'slotId' :'" + slotId + "', 'variant' : '" + variant+"'}");
    }

}
