package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.dto.composite.AttributeTableId;
import com.cloudofgoods.xenia.entity.xenia.AttributeTableEntity;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AttributeTableRepository extends MongoRepository<AttributeTableEntity, AttributeTableId> {

    Optional<AttributeTableEntity> findAllByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(String name, String organizationUuid);

    long countByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEquals(String toLowerCase, String organizationUuid);

    List<AttributeTableEntity> findAllByAttributeTableId_OrganizationUuidEquals(String organizationUuid, PageRequest of);

    long countByAttributeTableId_OrganizationUuidEquals(String organizationUuid);

    List<AttributeTableEntity> findAllByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableIdOrganizationUuidEquals(String toLowerCase, String organizationUuid);

    List<AttributeTableEntity> findAllByAttributeTableIdOrganizationUuidEquals(String organizationUuid);

    List<AttributeTableEntity> findAllByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameStartingWith(String organizationUuid, String toLowerCase, PageRequest of);

    Optional<AttributeTableEntity> findByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(String attributeTableName, String organizationUuid);

    Optional<AttributeTableEntity> findByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameEquals(String organizationUuid, String attributeTableName);
}
