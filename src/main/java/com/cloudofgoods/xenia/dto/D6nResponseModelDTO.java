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

    List<String> satisfiedConditions = new ArrayList<>();
    private String abTestEnable;
    private Double abTestPercentage;
    private String abTestStartDate;
    private String abTestEndDateTime;
    private String slotId;
    private String variant;
    private double priority;
    private double totalCount;
    private String segmentName;

    public void addToResponse(String segmentName, String abTestEnable, Double abTestPercentage, String abTestStartDate, String abTestEndDateTime, double priority, String slotId, String variant) {
        this.abTestEnable = abTestEnable;
        this.abTestPercentage = abTestPercentage;
        this.abTestStartDate = abTestStartDate;
        this.abTestEndDateTime = abTestEndDateTime;
        this.slotId = slotId;
        this.variant = variant;
        this.priority = priority;
        this.segmentName = segmentName;
        this.satisfiedConditions.add("{ 'priority' : '" + priority + "' ,'slotId' :'" + slotId + "', 'variant' : '" + variant + "'}");
    }

//    public void addToResponse(double priority, String slotId, String variant) {
//        this.variant = variant;
//        this.slotId = slotId;
//        this.priority = priority;
//        this.satisfiedConditions.add("{ 'priority' : '" + priority + "' ,'slotId' :'" + slotId + "', 'variant' : '" + variant+"'}");
//    }

}
