package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.composite.ChannelsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "channel_entity")
@AllArgsConstructor
@NoArgsConstructor
public class ChannelEntity implements SuperEntity {

    @Id
    private ChannelsId channelsId;
    @Indexed
    private String channelUuid;
    private String type;

    public ChannelEntity(String organizationUuid, String channelName, String channelUuid, String type) {
        this.channelsId = new ChannelsId(organizationUuid, channelName);
        this.channelUuid = channelUuid;
        this.type = type;
    }
}
