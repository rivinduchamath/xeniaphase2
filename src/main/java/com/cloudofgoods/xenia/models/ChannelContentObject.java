package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.config.customAnnotations.NotEmptyOrNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;

@Data
public class ChannelContentObject {
    @NotEmptyOrNull
    private String entryId;
    @NotEmptyOrNull
    private String variantId;
    @TextIndexed
    private String fullRuleString;

}
