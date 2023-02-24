package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.xenia.ChannelEntity;
import lombok.Data;

import java.util.List;

@Data
public class CustomChannelsObject {
    private List <ChannelEntity> channels;
    private long size;
}
