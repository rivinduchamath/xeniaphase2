package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.AudienceEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AudienceRepository extends MongoRepository<AudienceEntity, String> {

    long countAllByOrganizationUuidEqualsAndStatusEquals(String organizationUuid  , boolean bool);

    Optional<AudienceEntity> findByAudienceUuidEqualsAndOrganizationUuidEquals(String audienceUuid, String organizationUuid);

    List<AudienceEntity> findAllByOrganizationUuidEqualsAndAudienceNameStartingWithAndStatusEquals(String organizationId, String audienceName, boolean bool, PageRequest of);

    List<AudienceEntity> findByOrganizationUuidEqualsAndAudienceNameStartingWithAndStatusEquals(String organizationId, String audienceName, boolean bool);
}
