package com.cloudofgoods.xenia.repository.redis;

import com.cloudofgoods.xenia.entity.redis.ResponseRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRedisRepository extends CrudRepository <ResponseRedisEntity, String> {

}

