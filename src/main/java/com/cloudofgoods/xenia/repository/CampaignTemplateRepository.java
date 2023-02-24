package com.cloudofgoods.xenia.repository;


import com.cloudofgoods.xenia.entity.xenia.CampaignTemplateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CampaignTemplateRepository extends MongoRepository <CampaignTemplateEntity, String> {
}
