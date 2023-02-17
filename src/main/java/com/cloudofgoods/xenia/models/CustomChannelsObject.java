package com.cloudofgoods.xenia.models;

import com.cloudofgoods.xenia.entity.xenia.ChannelsObjects;
import lombok.Data;

import java.util.List;

@Data
public class CustomChannelsObject {
    private List <ChannelsObjects> channels;
    private long size;
}
