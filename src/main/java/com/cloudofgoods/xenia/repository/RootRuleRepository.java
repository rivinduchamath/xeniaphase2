package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import com.cloudofgoods.xenia.util.RuleStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RootRuleRepository extends MongoRepository <RuleRequestRootEntity, String> {
    List<RuleRequestRootEntity> findByStatusEnumAndEndDateTimeGreaterThan(RuleStatus active, Date date);

    List<RuleRequestRootEntity> findByOrganizationIdEquals(String organizationId, PageRequest of);
}
