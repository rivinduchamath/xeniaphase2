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

    long countByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWithAndStatusEquals(String organizationUuid, String toUpperCase, boolean bol);

    List<ChannelEntity> findByChannelsId_OrganizationUuidEquals(String organizationId);


    Optional<ChannelEntity> findByChannelsId_OrganizationUuidEqualsAndChannelUuidEquals(String organizationUuid, String channelUuid);

    List<ChannelEntity> findByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWithAndStatusEquals(String organizationUuid, String toUpperCase, boolean bol, PageRequest of);

    List<ChannelEntity> findByChannelsId_OrganizationUuidEqualsAndChannelsIdChannelsNameStartingWithAndStatusEquals(String organizationUuid, String toUpperCase, boolean bol);
}
