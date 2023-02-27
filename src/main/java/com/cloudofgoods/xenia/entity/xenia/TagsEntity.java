package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.dto.composite.TagsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tags_entity")
public class TagsEntity {
    @Id
    private TagsId tagsId;

    public TagsEntity(String tagName, String organizationUuid) {
        tagsId = new TagsId(organizationUuid, tagName);
    }
}
