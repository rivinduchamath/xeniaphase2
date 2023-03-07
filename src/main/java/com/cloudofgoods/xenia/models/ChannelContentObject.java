package com.cloudofgoods.xenia.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChannelContentObject {
    private String entryId;
    private String variantId;
//    private String abTestEnable;
//    private String abTestPercentage;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
//    private Date abTestStartDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "UTC")
//    private Date abTestEndDateTime;

}
