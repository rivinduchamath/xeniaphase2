package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.models.composite.ChannelsId;
import com.cloudofgoods.xenia.entity.xenia.ChannelsObjects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends MongoRepository <ChannelsObjects, ChannelsId> {
    ChannelsObjects findByUuid(String uuid);

    void deleteByUuid(String channelUuId);

//    Page<ChannelsObjects> findAllByChannelsIdChannelsNameStartingWithAndChannelsIdOrganization(String channelName, String organization, PageRequest of);
}
