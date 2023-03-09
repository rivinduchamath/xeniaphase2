package com.cloudofgoods.xenia.repository.analytics;

import com.cloudofgoods.xenia.entity.xenia.analytics.OrganizationAnalyticsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationAnalyticsRepository extends CrudRepository<OrganizationAnalyticsEntity, String> {
}
