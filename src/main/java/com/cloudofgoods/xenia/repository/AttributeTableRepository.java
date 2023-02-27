package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.dto.composite.AttributeTableId;
import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AttributeTableRepository extends MongoRepository<AttributeTableEntity, AttributeTableId> {
    Page<AttributeTableEntity> findAllByAttributeTableId_AttributeTableNameStartingWith(String name, PageRequest of);

    long countByAttributeTableId_AttributeTableNameStartingWith(String name);

    Optional<AttributeTableEntity> findAllByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(String name, String organizationUuid);

    Page<AttributeTableEntity> findAllByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEquals(String toLowerCase, String organizationUuid, PageRequest of);

    long countByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEquals(String toLowerCase, String organizationUuid);

    Page<AttributeTableEntity> findAllByAttributeTableId_OrganizationUuidEquals(String organizationUuid, PageRequest of);

    long countByAttributeTableId_OrganizationUuidEquals(String organizationUuid);
}
