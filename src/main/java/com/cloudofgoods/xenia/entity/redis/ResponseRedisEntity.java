package com.cloudofgoods.xenia.entity.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("response")
@Data
public class ResponseRedisEntity implements Serializable {

    @Id
    private String id;
    private String variant;
    private String user;
    private String slot;

}
