package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.models.composite.AttributesId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "audience_entity")
@AllArgsConstructor
@NoArgsConstructor
public class AttributeEntity {
    @Id
//    @Indexed(unique = true , name = "attribute_id")
    private AttributesId attributesId;
    @Indexed(name = "display_name")
    private String displayName;
    @Indexed
    private String type;
    @Indexed
    private List<Object> values;
    @Indexed(name = "attribute_uuid")
    private String attributeUuid;
    @Indexed(name = "table_name")
    private String tableName;
}
