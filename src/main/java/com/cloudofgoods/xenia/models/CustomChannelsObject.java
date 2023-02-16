package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.xenia.ChannelsEntity;
import lombok.Data;

import java.util.List;

@Data
public class CustomChannelsObject {
    private List <ChannelsEntity> channels;
    private long size;
}
