package com.cloudofgoods.xenia.repository.analytics;

import com.cloudofgoods.xenia.entity.xenia.UserAnalyticsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseUserRepository extends CrudRepository<UserAnalyticsEntity, String> {

}

