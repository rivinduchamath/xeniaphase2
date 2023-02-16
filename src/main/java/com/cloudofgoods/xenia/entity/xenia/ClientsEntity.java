package com.cloudofgoods.xenia.entity.xenia;

import com.cloudofgoods.xenia.entity.SuperEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "clients")
public class ClientsEntity implements SuperEntity {

    @Id
    private String id;
    private String name;
    private String uuid;
    @Indexed(unique = true)
    private String userName;
    private String password;
    private String organizationId;
    private List<String> userTypes;
}
