package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.RuleRequestRootDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.*;
import com.cloudofgoods.xenia.models.*;
import com.cloudofgoods.xenia.repository.ChannelRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.repository.RootRuleRepository;
import com.cloudofgoods.xenia.repository.TemplateRepository;
import com.cloudofgoods.xenia.service.RuleService;
import com.cloudofgoods.xenia.util.RuleStatus;
import com.cloudofgoods.xenia.util.Utility;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.lang.api.DescrFactory;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.mvel.DrlDumper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final RootRuleRepository rootRuleRepository;
    private final DroolServiceImpl droolService;
    @Autowired
    private InternalKnowledgeBase internalKnowledgeBase;
    private final OrganizationRepository organizationRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Value("${knowledge.package.name}")
    private String packageName;

    @Override
    public ServiceResponseDTO updateRules(List<String> ruleRootModel) {
        log.info("LOG :: RuleServiceImpl saveOrUpdateRuleListRules ");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            List<RuleRequestRootEntity> ruleRequestRootEntities = new ArrayList<>();
            Collections.reverse(ruleRootModel);
            for (String ruleRequestRootEntity : ruleRootModel) {
                Optional<RuleRequestRootEntity> ruleRequestRootEntity1 = rootRuleRepository.findById(ruleRequestRootEntity);

                RuleRequestRootEntity ruleRequestRoot1 = null;
                if (ruleRequestRootEntity1.isPresent()) {
                    RuleRequestRootDTO ruleRequestRoot = new RuleRequestRootDTO();
                    ruleRequestRoot.setId(ruleRequestRootEntity1.get().getId());
                    ruleRequestRoot.setChannels(ruleRequestRootEntity1.get().getChannels());
                    ruleRequestRoot.setTags(ruleRequestRootEntity1.get().getTags());
                    ruleRequestRoot.setCampaignDescription(ruleRequestRootEntity1.get().getCampaignDescription());
                    ruleRequestRoot.setCampaignName(ruleRequestRootEntity1.get().getCampaignName());
                    ruleRequestRoot.setPriority(ruleRequestRootEntity1.get().getPriority());
                    ruleRequestRoot.setEndDateTime(ruleRequestRootEntity1.get().getEndDateTime());
                    ruleRequestRoot.setStartDateTime(ruleRequestRootEntity1.get().getStartDateTime());
                    ruleRequestRoot.setOrganizationId(ruleRequestRootEntity1.get().getOrganizationId());
                    ruleRequestRoot.setChannelIds(ruleRequestRootEntity1.get().getChannelIds());
                    ruleRequestRoot.setStatus(ruleRequestRootEntity1.get().getStatusEnum());
                    ruleRequestRoot1 = updateRootRuleRepository(ruleRequestRoot, ruleRequestRootEntity1.get());
                }
                ruleRequestRootEntities.add(ruleRequestRoot1);
            }
            serviceResponseDTO.setData(ruleRequestRootEntities);
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setDescription("RuleServiceImpl saveOrUpdateRuleListRules() Success ");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: RuleServiceImpl saveOrUpdateRuleListRules() exception: " + Arrays.toString(exception.getStackTrace()));
            serviceResponseDTO.setError("RuleServiceImpl saveOrUpdateRuleListRules() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setDescription("RuleServiceImpl saveOrUpdateRuleListRules() exception ");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }

//    @Override
//    @Transactional
//    public ServiceResponseDTO saveOrUpdateRule(RuleRequestRootDTO ruleRootModel) {
//        log.info ("LOG :: RuleServiceImpl saveOrUpdateRuleListRules ");
//        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
//        try {
//            RuleRequestRootEntity ruleRequestRootEntity = saveOrUpdateSingleRuleFromMultipleRules (ruleRootModel);
//            serviceResponseDTO.setData (ruleRequestRootEntity);
//            serviceResponseDTO.setMessage ("Success");
//            serviceResponseDTO.setDescription ("RuleServiceImpl saveOrUpdateRuleListRules() Success ");
//            serviceResponseDTO.setCode ("2000");
//            serviceResponseDTO.setHttpStatus ("OK");
//
//            return serviceResponseDTO;
//        } catch (Exception exception) {
//            log.info ("LOG :: RuleServiceImpl saveOrUpdateRuleListRules() exception: " + Arrays.toString (exception.getStackTrace ()));
//            serviceResponseDTO.setError ("RuleServiceImpl saveOrUpdateRuleListRules() exception " + exception.getMessage ());
//            serviceResponseDTO.setMessage ("Fail");
//            serviceResponseDTO.setDescription ("RuleServiceImpl saveOrUpdateRuleListRules() exception ");
//            serviceResponseDTO.setCode ("5000");
//            serviceResponseDTO.setHttpStatus ("OK");
//            return serviceResponseDTO;
//        }
//    }


    // Save SegmentsObject Save Or Update
    @Override
    public RuleRequestRootEntity saveOrUpdateSingleRule(RuleRequestRootDTO ruleRequestRootModel) {
        log.info("LOG:: RuleServiceImpl saveOrUpdateSingleRule()");

        if (ruleRequestRootModel.getId() != null) { // Update ( Value Comes With id)
            log.info("LOG:: RuleServiceImpl saveOrUpdateSingleRule() If Get Id != null Update SegmentsObject." + ruleRequestRootModel.getId());
            RuleRequestRootEntity pastRootRule = findRootRuleById(ruleRequestRootModel.getId());
            if (pastRootRule != null) {
                log.info("LOG:: RuleServiceImpl saveOrUpdateSingleRule() ruleRequestRootModel.getRuleRequest()");
                return updateRootRuleRepository(ruleRequestRootModel, pastRootRule);
            }
            return null;
            // Save In Mongo Database and id Success Mongo save It  will send to the Save In Knowledge Builder to save
        } else {
            return saveRootRuleRepository(ruleRequestRootModel);
        }//end else
    }

    private RuleRequestRootEntity updateRootRuleRepository(RuleRequestRootDTO ruleRequestRootModel, RuleRequestRootEntity pastRootRule) {
        RuleRequestRootEntity rootRules = new RuleRequestRootEntity();
        // SegmentsObject Find By id. If Not Found It will Return Mono Error Otherwise it goes to  save Step
        // Creator Is Common For  All Rules
        rootRules.setId(ruleRequestRootModel.getId());
        rootRules = rootModelDataMethod(ruleRequestRootModel);
        List<SegmentsObject> ruleEntityRequestList = new ArrayList<>(); // List To collect Multiple Rules
        // Create String builder to collect Rules Strings and imports

        String imports = droolService.createImports();
        log.info("LOG:: RuleServiceImpl saveOrUpdateRuleManyRules() Save SegmentsObject imports ");
        // For Loop To Collect Multiple Rules To List
        List<RuleChannelObject> ruleChannelObjectList = new ArrayList<>();
        ruleRequestRootModel.getChannels().forEach(ruleChannelObject -> {
            ruleChannelObject.getAudienceObjects().forEach(audienceObject -> {
                List<SegmentsObject> segmentsObjects = new ArrayList<>();
                audienceObject.getSegments().forEach(segments -> {
                    String drlString = createDrlString(segments, ruleRequestRootModel);
                    segments.setSegmentRuleString(imports + " " + drlString);
                    segmentsObjects.add(segments);// Add To List
                });
                audienceObject.setSegments(segmentsObjects);
            });
            ruleChannelObjectList.add(ruleChannelObject);
        });
        rootRules.setChannels(ruleChannelObjectList);
        log.info("LOG:: RuleServiceImpl saveOrUpdateRuleManyRules() before return ");
        droolService.feedKnowledgeBuilderWhenUpdate(rootRules, pastRootRule);
        return rootRuleRepository.save(rootRules); // Return Updated RuleRequestRootEntity object
    }

    private RuleRequestRootEntity saveRootRuleRepository(RuleRequestRootDTO ruleRequestRootModel) {
        // Save (Value Comes Without id)
        log.info("LOG:: RuleServiceImpl saveRootRuleRepository() If Get Id = null Save SegmentsObject ruleRequestRootModel");
        // Add Common Things to all Rules
        RuleRequestRootEntity rootRules = rootModelDataMethod(ruleRequestRootModel);
        if (ruleRequestRootModel.getChannels() != null) {
            // Call method to collect Imports and add  to string Buffer
            String imports = droolService.createImports();
            log.info("LOG:: RuleServiceImpl saveRootRuleRepository() Save SegmentsObject imports : " + imports);
            List<RuleChannelObject> ruleChannelObjectList = new ArrayList<>();
            ruleRequestRootModel.getChannels().forEach(ruleChannelObject -> {
                List<AudienceObject> audiencesList = new ArrayList<>();
                ruleChannelObject.getAudienceObjects().forEach(audienceObject -> {
                    List<SegmentsObject> segmentsObjectsList = new ArrayList<>();
                    audienceObject.getSegments().forEach(segments -> {

                        segments.setPriority(segments.getPriority() == 0 ? 999999999 : (ruleRequestRootModel.getPriority() * 100000) + segments.getPriority());

                        Optional.ofNullable(segments.getSegmentName())
                                .ifPresent(name -> segments.setSegmentName(saveSegmentNameGenerator(name)));

                        String drlString = createDrlString(segments, ruleRequestRootModel);

                        segments.setFullRuleString(imports + "\n" + drlString);
                        segmentsObjectsList.add(segments);// Add To List
                    });
                    audienceObject.setSegments(segmentsObjectsList);
                    audiencesList.add(audienceObject);
                });
                ruleChannelObject.setAudienceObjects(audiencesList);
                ruleChannelObjectList.add(ruleChannelObject);
            });
            rootRules.setChannels(ruleChannelObjectList);
            log.info("LOG:: RuleServiceImpl saveRootRuleRepository() Save SegmentsObject stringBuilder ");
        }
        droolService.feedKnowledge(rootRules);
        return rootRuleRepository.save(rootRules);
    }

    //Find Root SegmentsObject With Multiple Child Rules From SegmentsObject ID
    public RuleRequestRootEntity findRootRuleById(String ruleId) {
        log.info("Find Root SegmentsObject By id " + ruleId);
        Optional<RuleRequestRootEntity> byId = rootRuleRepository.findById(ruleId);
        return byId.orElse(null);
    }

    //Remove Rules From Knowledge Base And Database From SegmentsObject ID
    public ServiceResponseDTO removeRuleFromKBAndDatabase(String segmentName) {
        ;
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase()" + segmentName);
        try {

            log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase() ruleEntity Name " + segmentName);
            internalKnowledgeBase.removeRule(packageName, segmentName);
            serviceResponseDTO.setDescription("removeRuleFromKBAndDatabase Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl saveTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO updateCampaignStatus(String campaignId, String status) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        RuleRequestRootEntity ruleRequestRootEntity = findRootRuleById (campaignId);
        if ((RuleStatus.INACTIVE.toString ().equals (status)) && ruleRequestRootEntity.getStatusEnum ().equals (RuleStatus.ACTIVE)) {
            log.info ("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase()" + campaignId);
//            for (SegmentsObject ruleEntity : ruleRequestRootEntity.getSegments ()) {
//                log.info ("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase() ruleEntity Name " + ruleEntity.getSegmentName ());
//                internalKnowledgeBase.removeRule (packageName, ruleEntity.getSegmentName ());
//            }
            ruleRequestRootEntity.setStatusEnum (RuleStatus.valueOf (status));
            RuleRequestRootEntity save = rootRuleRepository.save (ruleRequestRootEntity);
            serviceResponseDTO.setData (save);
            serviceResponseDTO.setDescription ("updateCampaignStatus Success");
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }else if ((RuleStatus.ACTIVE.toString ().equals (status)) && (ruleRequestRootEntity.getStatusEnum ().equals (RuleStatus.INACTIVE))) {
            ruleRequestRootEntity.setStatusEnum (RuleStatus.valueOf (status));
            droolService.feedKnowledge (ruleRequestRootEntity);
            RuleRequestRootEntity save = rootRuleRepository.save (ruleRequestRootEntity);
            serviceResponseDTO.setData (save);
            serviceResponseDTO.setDescription ("updateCampaignStatus Success");
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
        return serviceResponseDTO;
//        }else {
//            return null;
        }
        return null;
    }

    private String saveSegmentNameGenerator(String name) {
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        UUID firstUUID = timeBasedGenerator.generate();
        CampaignTemplateEntity templateEntity = new CampaignTemplateEntity();
        try {
            String separator = "##$$$##";
            int sepPos = name.lastIndexOf(separator);
            if (sepPos == -1) {
                System.out.println();
            }
            name = name.substring(0, sepPos);
            name = name + "##$$$##" + firstUUID.timestamp();
        } catch (Exception e) {
            name = name + "##$$$##" + firstUUID.timestamp();
        }


        String separator = "##$$$##";
        int sepPos = name.lastIndexOf(separator);
        if (sepPos != -1) {
            name = name.substring(0, sepPos);
        }
        name = name + separator + firstUUID.timestamp();

        log.info("LOG:: Segment String :" + name);
        templateEntity.setCampTemplateName(name);
        return name;
    }

    @Override
    public ServiceResponseDTO findByID(String id) {
        Optional<RuleRequestRootEntity> byId = rootRuleRepository.findById(id);
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        if (byId.isPresent()) {
            serviceResponseDTO.setData(byId);
            serviceResponseDTO.setDescription("findByID Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
        } else {
            serviceResponseDTO.setData(byId);
            serviceResponseDTO.setDescription("findByID Success No Data");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
        }
        return serviceResponseDTO;
    }

    RuleRequestRootEntity rootModelDataMethod(RuleRequestRootDTO ruleRequestRootModel) {
        Instant now = Instant.now(); // 2023-10-15T11:22:11.000Z
        RuleRequestRootEntity rootRules = new RuleRequestRootEntity();
        if (ruleRequestRootModel.getCampaignId() != null) rootRules.setCampaignId(ruleRequestRootModel.getCampaignId());
        if (ruleRequestRootModel.getCampaignName() != null)
            rootRules.setCampaignName(ruleRequestRootModel.getCampaignName());
        if (ruleRequestRootModel.getCampaignDescription() != null)
            rootRules.setCampaignDescription(ruleRequestRootModel.getCampaignDescription());
        if (ruleRequestRootModel.getStartDateTime() != null)
            rootRules.setStartDateTime(ruleRequestRootModel.getStartDateTime());
        if (ruleRequestRootModel.getEndDateTime() != null)
            rootRules.setEndDateTime(ruleRequestRootModel.getEndDateTime());
        if (ruleRequestRootModel.getOrganizationId() != null)
            rootRules.setOrganizationId(ruleRequestRootModel.getOrganizationId());
        if (ruleRequestRootModel.getPriority() != null) rootRules.setPriority(ruleRequestRootModel.getPriority());
        if (ruleRequestRootModel.getChannelIds() != null) rootRules.setChannelIds(ruleRequestRootModel.getChannelIds());
        if (ruleRequestRootModel.getTags() != null) rootRules.setTags(ruleRequestRootModel.getTags());
        if (ruleRequestRootModel.getStatus() != null) rootRules.setStatusEnum(ruleRequestRootModel.getStatus());
        rootRules.setUpdatedDate(Date.from(now));
        return rootRules;
    }

    // Create Drool String to Create Multiple Rules
    public String createDrlString(SegmentsObject ruleEntity, RuleRequestRootDTO ruleRequestRootModel) {
        boolean channelValidate = false;
        log.info("LOG:: DroolServiceImpl createDrlString() Inside Method ");

        String fact = ruleEntity.getSegmentRuleString();//"\"saman\".equals(userData.get(\"name\")) && 12 < userData.get(\"age\")";

        double priority = ruleEntity.getPriority();
        try {
            Optional<OrganizationEntity> organization = organizationRepository.findByUuid(ruleRequestRootModel.getOrganizationId());
  if (organization.isPresent()) {
      List<ChannelsObjects> channelsObjectsList = organization.get().getChannelsObjects();
      ChannelsObjects channelObj = new ChannelsObjects();
      for (ChannelsObjects channelsObject : channelsObjectsList) {
          if (channelsObject.getUuid().equals(ruleEntity.getChannelId())) {
              channelValidate = true;
              channelObj = channelsObject;
              break;
          }
      }
      if (channelValidate) {
          for (ChannelContentObject channelContentObject : ruleEntity.getEntryVariantMapping()) {
              log.info("LOG:: DroolServiceImpl createDrlString() priority: " + priority);
              String metadata =
                      "startDate > \"" + Utility.formatter.format(ruleRequestRootModel.getStartDateTime()) +
                              "\" && endDate < \"" + Utility.formatter.format(ruleRequestRootModel.getEndDateTime()) +
                              "\" && contextId.contains(" + "\"" + channelContentObject.getEntryId().toUpperCase() +
                              "\"" + ") && (<channel>)";
              log.info("LOG:: DroolServiceImpl createDrlString() metadata: " + metadata);
              StringBuilder channel = new StringBuilder();

              channel.append("channels.contains(").append("\"").append(channelObj.getUuid().toUpperCase()).append("\"").append(")").append(" || ");

              String metaString = metadata.replace("<channel>", channel.substring(0, channel.length() - 4));
              PackageDescrBuilder pkg = DescrFactory.newPackage();
              PackageDescrBuilder pkgDescBuilder = pkg.end();
              pkgDescBuilder.newRule().name(ruleEntity.getSegmentName()).attribute(
                      "salience", priority + "").attribute("agenda-group", "\"" +
                      ruleRequestRootModel.getOrganizationId().toUpperCase() +
                      "\"").lhs().pattern(
                      "$user : User").constraint(fact + "").end().pattern(
                      "$meta : MetaData").constraint(metaString).end().end().rhs(
                      "response.addToResponse(" + ruleEntity.getPriority() +
                              ",\"" + channelContentObject.getEntryId().toUpperCase() + "\",\"" +
                              channelContentObject.getVariantId() + "\");").end();

              PackageDescr packageDescr = pkgDescBuilder.getDescr();
              DrlDumper dumper = new DrlDumper();

              String drlFile = dumper.dump(packageDescr);
              log.info("LOG:: DroolServiceImpl createDrlString() Single drlFile :" + drlFile);
              drlFile = drlFile.replaceFirst("package ", "");
              if (ruleEntity.isTemplate()) {
                  saveTemplate(ruleEntity.getSegmentDescription(), ruleEntity.getSegmentName(), ruleEntity.getRuleObject());
              }
              return drlFile;
          }
      }
  }

        } catch (Exception e) {
            log.error("LOG:: DroolServiceImpl createDrlString() Exception :" + e.getMessage());
            return "...............Error Drool String Creation...............";
        }
        return null;
    }

    private void saveTemplate(String getSegmentationDescription, String segmentName, NodeObject fact) {
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        UUID firstUUID = timeBasedGenerator.generate();
        TemplateEntity templateEntity = new TemplateEntity();
        segmentName = segmentName + "##$$$##" + firstUUID.timestamp();
        templateEntity.setSegmentName(segmentName);
        templateEntity.setSegmentationDescription(getSegmentationDescription);
        templateEntity.setFact(fact);
        TemplateEntity save = templateRepository.save(templateEntity);

    }
}
