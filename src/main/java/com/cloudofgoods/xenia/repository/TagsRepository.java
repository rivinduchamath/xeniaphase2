package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.dto.composite.TagsId;
import com.cloudofgoods.xenia.entity.xenia.TagsEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TagsRepository extends MongoRepository<TagsEntity, TagsId> {

    Optional<TagsEntity> findByTagsIdTagsNameEqualsAndTagsIdOrganizationUuidEquals(String tagsName, String organizationUuid);

    List<TagsEntity> findAllByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(String organizationUuid, String tagsName, PageRequest of);

    long countByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(String organizationUuid, String tagsName);

    List<TagsEntity> findAllByTagsIdOrganizationUuidEquals(String organizationUuid, PageRequest of);

    long countByTagsIdOrganizationUuidEquals(String organizationUuid);

    List<TagsEntity> findAllByTagsId_OrganizationUuidEqualsAndTagsIdTagsNameStartsWith(String organizationUuid, String tagsName);

    Optional<TagsEntity> findAllByTagsId_OrganizationUuidEqualsAndTagsIdTagsNameEquals(String organizationUuid, String tagsName);
}
