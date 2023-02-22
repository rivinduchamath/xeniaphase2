package com.cloudofgoods.xenia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class D6nResponseModelDTO implements Serializable {

    private String slotId;
    private String variant;
    private double priority;
    List<String> satisfiedConditions =  new ArrayList<>();
    private double totalCount;

    public void addToResponse(double priority, String slotId, String variant) {
//        System.out.println("priority : " + priority + " Slot :" + slotId + " ChannelContentObject : " + variant);
        this.variant = variant;
        this.slotId = slotId;
        this.priority = priority;
        String val = "{ \"priority\" : \"" + priority + "\" ,\"Slot\" :\"" + slotId + "\", \"ChannelContentObject\" : \"" + variant+"\"}";
        this.satisfiedConditions.add(val);

    }

}
