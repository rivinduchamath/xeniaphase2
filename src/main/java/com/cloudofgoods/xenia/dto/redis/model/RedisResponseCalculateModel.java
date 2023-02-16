package com.cloudofgoods.xenia.dto.redis.model;

import com.cloudofgoods.xenia.dto.D6nResponseModelDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisResponseCalculateModel {
    private String key;
    private D6nResponseModelDTO d6nResponseModelDTO;
}
