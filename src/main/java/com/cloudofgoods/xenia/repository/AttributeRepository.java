package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.AttributeEntity;
import com.cloudofgoods.xenia.models.composite.AttributesId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeRepository extends MongoRepository <AttributeEntity, AttributesId> {
    Optional<AttributeEntity>  deleteAttributeByAttributeUuid(String attributeId);

    Optional<AttributeEntity> findByAttributeUuidEquals(String uuid);

    List<AttributeEntity> findAllByAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrAttributesIdOrganizationUuidEqualsAndTypeIn(String organization, String attributeName, String organization2, List<String> type, PageRequest of);

    long countByAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrTypeIn(String organizationUuid, String attributeName, List<String> type);

    List<AttributeEntity> findAllByAttributesId_OrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrAttributesIdOrganizationUuidEqualsAndTypeIn(String organizationUuid, String attributeName, String organizationUuid1, List<String> type);

   Optional<AttributeEntity> findByAttributesId_OrganizationUuidEqualsAndAttributeUuidEquals(String organizationUuid, String attributeUuid);

}
