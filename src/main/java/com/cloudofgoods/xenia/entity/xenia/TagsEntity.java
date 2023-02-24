package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.dto.composite.TagsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tags_entity")
public class TagsEntity {
    private TagsId tagsId;

}
