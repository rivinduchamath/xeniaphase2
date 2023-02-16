package com.cloudofgoods.xenia.entity.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("organizations-data")
@Data
public class OrganizationRedisEntity implements Serializable {
    @Id
    private String id;
    private String name;
}
