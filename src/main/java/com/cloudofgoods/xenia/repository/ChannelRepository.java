package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.models.composite.ChannelsId;
import com.cloudofgoods.xenia.entity.xenia.ChannelEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends MongoRepository <ChannelEntity, ChannelsId> {
    Optional<ChannelEntity> findByChannelUuidEquals(String uuid);

    Optional<ChannelEntity>  deleteByChannelUuidEquals(String channelUuId);

    List<ChannelEntity> findByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWith(String organizationUuid, String toUpperCase, PageRequest of);

    long countByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWith(String organizationUuid, String toUpperCase);

    List<ChannelEntity> findByChannelsId_OrganizationUuidEquals(String organizationId);
}
