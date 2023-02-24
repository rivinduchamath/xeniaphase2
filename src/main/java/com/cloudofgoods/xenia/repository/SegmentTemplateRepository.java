package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.SegmentTemplateEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SegmentTemplateRepository extends MongoRepository <SegmentTemplateEntity, String> {
    List<SegmentTemplateEntity> findAllBySegmentNameNotNull(PageRequest pageRequest);
    Optional<SegmentTemplateEntity> findAllBySegmentNameStartsWith(String name);

}
