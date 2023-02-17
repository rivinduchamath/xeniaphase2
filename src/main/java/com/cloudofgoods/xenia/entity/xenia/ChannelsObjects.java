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
@Document(collection = "channels")
@AllArgsConstructor
@NoArgsConstructor
public class ChannelsObjects implements SuperEntity {

    @Indexed(unique = true)
    private String channelsName;
    private String uuid;
}
