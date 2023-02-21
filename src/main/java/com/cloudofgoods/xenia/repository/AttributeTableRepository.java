package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttributeTableRepository extends MongoRepository<AttributeTableEntity, String> {
    Page<AttributeTableEntity> findAllByAttributeTableNameStartingWith(String name, PageRequest of);

    long countByAttributeTableNameStartingWith(String name);

//    List<AttributeTableEntity> findAllByAttributeTableNameNotEquals(PageRequest of);
}
