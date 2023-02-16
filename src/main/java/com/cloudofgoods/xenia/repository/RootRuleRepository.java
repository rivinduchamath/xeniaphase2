package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import com.cloudofgoods.xenia.util.RuleStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RootRuleRepository extends MongoRepository <RuleRequestRootEntity, String> {
//    List <RuleRequestRootEntity> findAllByEndDateTimeIsGreaterThanEqualAndSlotIdEquals(Date from, String slotId, PageRequest pageRequest); // If Composite RuleRequestRootModelId
    long countAllByEndDateTimeIsGreaterThanEqual(Date from);
    List<RuleRequestRootEntity> findByStatusEnumAndEndDateTimeGreaterThan(RuleStatus active, Date date);
}
