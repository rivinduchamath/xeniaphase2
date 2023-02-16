package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.models.AttributesObject;
import com.cloudofgoods.xenia.models.composite.AttributesId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends MongoRepository <AttributesObject, String> {
    void deleteAttributeByUuid(String attributeId);

    AttributesObject findByUuid(String uuid);

//    Page<AttributesObject> findAllByUuidEqualsAndAttributeNameStartingWithOrTypeIn(String organization, String attributeName, List<String> type, PageRequest of);

//    @Query(value = "{ select * from AttributesObject ar  }", fields = "{ 'name' : 1 }")
//    Page <AttributesObject> findAllByUuidEqualsAndAttributeNameStartingWithOrTypeIn(String organization, String attributeName, List<String> type, PageRequest of);
//    Page <AttributesObject> findAllByAttributeNameStartingWithAndAttributeIdOrganizationOrTypeInAndOrganization(String attributeName, String organizations, List <String> types, String organization, PageRequest pageRequest);


//    @Query(value = "select * from AttributeObject ao ")
//    Page<AttributesObject> findAllByUuidEqualsAndAttributeNameStartingWithOrTypeIn(String organization, String attributeName, List<String> type, Pageable pageable);

}
