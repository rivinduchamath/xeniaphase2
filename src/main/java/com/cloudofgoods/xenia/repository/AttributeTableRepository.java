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

    Optional<AttributeTableEntity> findByAttributeTableId_AttributeTableNameEqualsAndAttributeTableId_OrganizationUuidEquals(String attributeTableName, String organizationUuid);

    Optional<AttributeTableEntity> findByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameEquals(String organizationUuid, String attributeTableName);

    long countByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableId_OrganizationUuidEqualsAndStatusEquals(String toLowerCase, String organizationUuid, boolean b);

    List<AttributeTableEntity> findAllByAttributeTableId_OrganizationUuidEqualsAndAttributeTableId_AttributeTableNameStartingWithAndStatusEquals(String organizationUuid, String toLowerCase, boolean b, PageRequest of);

    List<AttributeTableEntity> findAllByAttributeTableId_AttributeTableNameStartingWithAndAttributeTableIdOrganizationUuidEqualsAndStatusEquals(String toLowerCase, String organizationUuid, boolean b);
}
