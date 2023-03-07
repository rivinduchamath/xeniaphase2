package com.cloudofgoods.xenia.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;

@Data
public class ChannelContentObject {
    private String entryId;
    private String variantId;
    @TextIndexed
    private String fullRuleString;

}
