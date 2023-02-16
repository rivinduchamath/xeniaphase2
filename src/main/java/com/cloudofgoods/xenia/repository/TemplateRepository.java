package com.cloudofgoods.xenia.repository;

import com.cloudofgoods.xenia.entity.xenia.TemplateEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TemplateRepository extends MongoRepository <TemplateEntity, String> {
    List<TemplateEntity> findAllBySegmentNameNotNull(PageRequest pageRequest);
    Optional<TemplateEntity> findAllBySegmentNameStartsWith(String name);

}
