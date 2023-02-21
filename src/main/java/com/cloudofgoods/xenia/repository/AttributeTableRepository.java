package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttributeTableRepository extends MongoRepository<AttributeTableEntity, String> {
    List<AttributeTableEntity> findAllByAttributeTableNameEquals(String name, PageRequest of);

    long countByAttributeTableNameEquals(String name);
}
