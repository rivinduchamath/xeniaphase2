package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.AttributesObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "organization")
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationEntity implements SuperEntity {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    @TextIndexed
    private String uuid;
    private String password;

//    private List<ChannelsObjects> channelsObjects;
//
//    private List <AttributesObject> attributesObject;
}
