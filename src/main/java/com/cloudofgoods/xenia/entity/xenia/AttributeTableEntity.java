package com.cloudofgoods.xenia.entity.xenia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "attribute_table")
public class AttributeTableEntity {

    @Id
    private String id;
    @Indexed(unique = true, name = "attribute_table_name")
    private String attributeTableName;

    @Indexed(name = "display_name")
    private String displayName;
}
