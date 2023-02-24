package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import com.cloudofgoods.xenia.models.NodeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "template")
public class SegmentTemplateEntity implements SuperEntity {

    @Id
    private String id;
    @Indexed(unique = true)
    private String segmentName;
    @TextIndexed
    private String segmentationDescription;
    private NodeObject fact;
    private String organizationUuid;
}
