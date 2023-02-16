package com.cloudofgoods.xenia.dto.caution;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MetaData {
    private List<String> contextId;
    private Date startDate;
    private Date endDate;
    private List<String> channels;
}
