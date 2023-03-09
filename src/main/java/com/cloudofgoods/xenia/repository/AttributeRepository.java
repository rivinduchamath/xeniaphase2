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
    Optional<AttributeEntity> findByAttributeUuidEquals(String uuid);

   Optional<AttributeEntity> findByAttributesId_OrganizationUuidEqualsAndAttributeUuidEquals(String organizationUuid, String attributeUuid);

    long countByStatusEqualsAndAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrAttributesIdOrganizationUuidEqualsAndTypeInAndStatusEquals(boolean b, String organizationUuid, String attributeName,String organizationUuid2, List<String> type, boolean bool);

    List<AttributeEntity> findAllByStatusEqualsAndAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrStatusEqualsAndAttributesIdOrganizationUuidEqualsAndTypeIn(boolean b, String organizationUuid, String attributeName,boolean c, String organizationUuid1, List<String> type, PageRequest of);

    List<AttributeEntity> findAllByStatusEqualsAndAttributesId_OrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrStatusEqualsAndAttributesIdOrganizationUuidEqualsAndTypeIn(boolean b, String organizationUuid, String attributeName,boolean c, String organizationUuid1, List<String> type);

   }
