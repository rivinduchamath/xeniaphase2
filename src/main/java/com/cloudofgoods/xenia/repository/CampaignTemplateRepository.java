package com.cloudofgoods.xenia.repository;


import com.cloudofgoods.xenia.entity.xenia.CampaignTemplateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface CampaignTemplateRepository extends MongoRepository <CampaignTemplateEntity, String> {
    Optional<CampaignTemplateEntity> findByIdAndOrganizationEquals(String id, String organizationUuid);
}
