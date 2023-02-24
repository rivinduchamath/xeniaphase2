package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends MongoRepository<OrganizationEntity, String> {

    Optional<OrganizationEntity> findByUuidEquals(String uuid);

    Optional<OrganizationEntity> findOrganizationEntityByUuidEquals(String uuid);

}