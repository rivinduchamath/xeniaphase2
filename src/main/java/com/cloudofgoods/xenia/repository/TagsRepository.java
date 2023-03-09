package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.dto.composite.TagsId;
import com.cloudofgoods.xenia.entity.xenia.TagsEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TagsRepository extends MongoRepository<TagsEntity, TagsId> {

    Optional<TagsEntity> findAllByTagsId_OrganizationUuidEqualsAndTagsIdTagsNameEquals(String organizationUuid, String tagsName);

    List<TagsEntity> findAllByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWithAndStatusEquals(String organizationUuid, String toUpperCase, boolean b, PageRequest of);

    List<TagsEntity> findAllByTagsId_OrganizationUuidEqualsAndTagsIdTagsNameStartsWithAndStatusEquals(String organizationUuid, String toUpperCase, boolean b);

    long countByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWithAndStatusEquals(String organizationUuid, String toUpperCase, boolean b);
}
