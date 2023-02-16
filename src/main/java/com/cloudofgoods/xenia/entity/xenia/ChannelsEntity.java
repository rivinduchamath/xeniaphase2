package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.composite.ChannelsId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "channels")
public class ChannelsEntity implements SuperEntity {
    @Id
    @Indexed
    private ChannelsId channelsId;
    private String uuid;
}
