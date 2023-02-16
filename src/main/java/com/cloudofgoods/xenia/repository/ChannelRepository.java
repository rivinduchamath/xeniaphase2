package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.models.composite.ChannelsId;
import com.cloudofgoods.xenia.entity.xenia.ChannelsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends MongoRepository <ChannelsEntity, ChannelsId> {
    ChannelsEntity findByUuid(String uuid);

    void deleteByUuid(String channelUuId);

    Page<ChannelsEntity> findAllByChannelsIdChannelsNameStartingWithAndChannelsIdOrganization(String channelName, String organization, PageRequest of);
}
