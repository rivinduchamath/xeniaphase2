package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.AudienceEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AudienceRepository extends MongoRepository<AudienceEntity, String> {
    AudienceEntity findByAudienceUuid(String audienceUuid);

    long countAllByOrganizationUuidEquals(String organizationUuid );

    List<AudienceEntity> findAllByOrganizationUuidEquals(String organizationId, PageRequest of);

    Optional<AudienceEntity> findByAudienceUuidEqualsAndOrganizationUuidEquals(String audienceUuid, String organizationUuid);

    List<AudienceEntity> findAllByOrganizationUuidEqualsAndAudienceNameStartingWith(String organizationId, String audienceName, PageRequest of);

    List<AudienceEntity> findByOrganizationUuidEqualsAndAudienceNameStartingWith(String organizationId, String audienceName);
}
