package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.NodeObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "template")
public class TemplateEntity implements SuperEntity {

    @Id
    private String id;
    @Indexed(unique = true)
    private String segmentName;
    @TextIndexed
    private String segmentationDescription;
    private NodeObject fact;
    private String organizationUuid;
}
