package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends MongoRepository <OrganizationEntity, String> {

    OrganizationEntity findByAttributesObjectUuid(String uuid);
    OrganizationEntity findByUuid(String uuid);
    OrganizationEntity findByUuidEqualsAndAttributesObjectAttributeNameStartingWithOrAttributesObjectTypeIn(String organization, String attributeName, List<String> type);
    OrganizationEntity findByUuidEqualsAndChannelsObjectsChannelsNameEquals(String organizationUuid, String attributeName);
    OrganizationEntity findOrganizationEntityByUuidEquals(String uuid);

    OrganizationEntity findByChannelsObjectsUuid(String channelUuid);

//    AttributesObject findByAttributesObject(String attributeUuid);

//    @Query("{ 'uuid': ?0, 'attributesObject': { '$elemMatch': { 'attributeName': { '$regex': ?1 }, 'type': { '$in': ?2 } } } }")
//    List<OrganizationEntity> findAllByUuidAndAttribute(String uuid, String attributeNameRegex, List<String> types);

}