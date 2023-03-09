package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.RuleRequestRootDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.*;
import com.cloudofgoods.xenia.entity.xenia.analytics.OrganizationAnalyticsEntity;
import com.cloudofgoods.xenia.models.*;
import com.cloudofgoods.xenia.repository.AudienceRepository;
import com.cloudofgoods.xenia.repository.ChannelRepository;
import com.cloudofgoods.xenia.repository.RootRuleRepository;
import com.cloudofgoods.xenia.repository.SegmentTemplateRepository;
import com.cloudofgoods.xenia.repository.analytics.OrganizationAnalyticsRepository;
import com.cloudofgoods.xenia.service.RuleService;
import com.cloudofgoods.xenia.util.RuleStatus;
import com.cloudofgoods.xenia.util.Utils;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.cloudofgoods.xenia.util.Utils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final RootRuleRepository rootRuleRepository;
    private final DroolServiceImpl droolService;
    private final ChannelRepository channelRepository;
    private final SegmentTemplateRepository segmentTemplateRepository;
    private final AudienceRepository audienceRepository;
    private final OrganizationAnalyticsRepository organizationAnalyticsRepository;
    @Autowired
    private InternalKnowledgeBase internalKnowledgeBase;
    @Value("${knowledge.package.name}")
    private String packageName;

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

    @Override
    public ServiceResponseDTO updateRules(List<String> ruleRootModel) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG :: RuleServiceImpl saveOrUpdateRuleListRules ");
        try {
            List<RuleRequestRootEntity> ruleRequestRootEntities = new ArrayList<>();
            Collections.reverse(ruleRootModel);
            for (String ruleRequestRootEntity : ruleRootModel) {
                Optional<RuleRequestRootEntity> ruleRequestRootEntityOpt = rootRuleRepository.findById(ruleRequestRootEntity);

                RuleRequestRootEntity ruleRequestRoot = null;
                if (ruleRequestRootEntityOpt.isPresent()) {
                    RuleRequestRootDTO ruleRequestRootDTO = new RuleRequestRootDTO();
                    ruleRequestRootDTO.setId(ruleRequestRootEntityOpt.get().getId());
                    ruleRequestRootDTO.setChannels(ruleRequestRootEntityOpt.get().getChannels());
                    ruleRequestRootDTO.setTags(ruleRequestRootEntityOpt.get().getTags());
                    ruleRequestRootDTO.setCampaignDescription(ruleRequestRootEntityOpt.get().getCampaignDescription());
                    ruleRequestRootDTO.setCampaignName(ruleRequestRootEntityOpt.get().getCampaignName());
                    ruleRequestRootDTO.setPriority(ruleRequestRootEntityOpt.get().getPriority());
                    ruleRequestRootDTO.setEndDateTime(ruleRequestRootEntityOpt.get().getEndDateTime());
                    ruleRequestRootDTO.setStartDateTime(ruleRequestRootEntityOpt.get().getStartDateTime());
                    ruleRequestRootDTO.setOrganizationId(ruleRequestRootEntityOpt.get().getOrganizationId());
                    ruleRequestRootDTO.setChannelIds(ruleRequestRootEntityOpt.get().getChannelIds());
                    ruleRequestRootDTO.setStatus(ruleRequestRootEntityOpt.get().getStatusEnum());
                    ruleRequestRoot = updateRootRuleRepository(ruleRequestRootDTO, ruleRequestRootEntityOpt.get());
                }
                ruleRequestRootEntities.add(ruleRequestRoot);
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

    // Save SegmentsObject Save Or Update
    @Override
    public RuleRequestRootEntity saveOrUpdateSingleRule(RuleRequestRootDTO ruleRequestRootModel) {
        log.info("LOG:: RuleServiceImpl saveOrUpdateSingleRule()");

        if (NotEmptyOrNullValidator.isNotNullOrEmpty(ruleRequestRootModel.getId())) { // Update ( Value Comes With id)
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

        String imports = getDroolImports();
        log.info("LOG:: RuleServiceImpl saveOrUpdateRuleManyRules() Save SegmentsObject imports ");
        // For Loop To Collect Multiple Rules To List
        List<RuleChannelObject> ruleChannelObjectList = new ArrayList<>();
        ruleRequestRootModel.getChannels().forEach(ruleChannelObject -> {
            ruleChannelObject.getAudienceObjects().forEach(audienceObject -> {
                if (audienceObject.isTemplate()) {
                    CompletableFuture.runAsync(() -> audienceSaveTemplate(
                            audienceObject,
                            ruleRequestRootModel.getOrganizationId())
                    );
                }
                List<SegmentsObject> segmentsObjects = new ArrayList<>();
                audienceObject.getSegments().forEach(segments -> {
                    segments.setSegmentRuleString(NotEmptyOrNullValidator.isNotNullOrEmpty(audienceObject.getAudienceRuleString()) ? audienceObject.getAudienceRuleString() + " && " + segments.getSegmentRuleString() : segments.getSegmentRuleString());

                    List<ExperiencesObject> experiencesObjects = new ArrayList<>();
                    segments.getExperiences().forEach(experiencesObject -> {
                        List<ChannelContentObject> channelContentObjects = new ArrayList<>();
                        experiencesObject.getEntryVariantMapping().forEach(channelContentObject -> {
                            String drlString = createDrlString(channelContentObject, segments, ruleRequestRootModel);
                            channelContentObject.setFullRuleString(imports + "\n" + drlString);
                            channelContentObjects.add(channelContentObject);
                        });
                        experiencesObject.setEntryVariantMapping(channelContentObjects);
                        experiencesObjects.add(experiencesObject);
                    });
                    segments.setExperiences(experiencesObjects);
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

    private void audienceSaveTemplate(AudienceObject audienceObject, String organizationId) {
        AudienceEntity audienceEntity = new AudienceEntity();
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        UUID firstUUID = timeBasedGenerator.generate();
        audienceEntity.setAudienceUuid(firstUUID.toString());
        if (audienceObject.getAudienceName() != null) audienceEntity.setAudienceName(audienceObject.getAudienceName());
        if (audienceObject.getAudienceDescription() != null)
            audienceEntity.setAudienceDescription(audienceObject.getAudienceDescription());
        if (audienceObject.getAudienceRuleString() != null)
            audienceObject.setAudienceRuleString(audienceObject.getAudienceRuleString());
        if (organizationId != null) audienceEntity.setOrganizationUuid(organizationId);
        if (audienceObject.getAudienceObject() != null)
            audienceEntity.setAudienceObject(audienceObject.getAudienceObject());
        audienceRepository.save(audienceEntity);
    }

    private RuleRequestRootEntity saveRootRuleRepository(RuleRequestRootDTO ruleRequestRootModel) {
        // Save (Value Comes Without id)
        log.info("LOG:: RuleServiceImpl saveRootRuleRepository() If Get Id = null Save SegmentsObject ruleRequestRootModel");
        // Add Common Things to all Rules
        RuleRequestRootEntity rootRules = rootModelDataMethod(ruleRequestRootModel);
        if (NotEmptyOrNullValidator.isNullOrEmptyList(ruleRequestRootModel.getChannels())) {
            // Call method to collect Imports and add  to string Buffer
            String imports = getDroolImports();
            log.info("LOG:: RuleServiceImpl saveRootRuleRepository() Save SegmentsObject imports : \n" + imports);
            List<RuleChannelObject> ruleChannelObjectList = new ArrayList<>();
            ruleRequestRootModel.getChannels().parallelStream().forEach(ruleChannelObject -> {
                List<AudienceObject> audiencesList = new ArrayList<>();
                ruleChannelObject.getAudienceObjects().parallelStream().forEach(audienceObject -> {
                    List<SegmentsObject> segmentsObjectsList = new ArrayList<>();
                    audienceObject.getSegments().parallelStream().forEach(segments -> {

                        segments.setPriority(segments.getPriority() == 0 ? 999999999 : (ruleRequestRootModel.getPriority() * 100000) + segments.getPriority());
                        segments.setSegmentRuleString(NotEmptyOrNullValidator.isNotNullOrEmpty(audienceObject.getAudienceRuleString()) ? audienceObject.getAudienceRuleString() + " && " + segments.getSegmentRuleString() : segments.getSegmentRuleString());

                        List<ExperiencesObject> experiencesObjects = segments.getExperiences().parallelStream()
                                .peek(experiencesObject -> {
                                    List<ChannelContentObject> channelContentObjects = experiencesObject.getEntryVariantMapping().parallelStream()
                                            .peek(channelContentObject -> {
                                                String drlString = createDrlString(channelContentObject, segments, ruleRequestRootModel);
                                                channelContentObject.setFullRuleString(imports + "\n" + drlString);
                                            })
                                            .collect(Collectors.toList());
                                    experiencesObject.setEntryVariantMapping(channelContentObjects);
                                })
                                .collect(Collectors.toList());
                        segments.setExperiences(experiencesObjects);
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
            droolService.feedKnowledge(rootRules);
            CompletableFuture.runAsync(() -> campaignsWithOrganizationWithSave(
                    ruleRequestRootModel)
            );
            return rootRuleRepository.save(rootRules);
        }
        return null;
    }

    private void campaignsWithOrganizationWithSave(RuleRequestRootDTO ruleRequestRootModel) {
        OrganizationAnalyticsEntity organizationAnalyticsEntity = new OrganizationAnalyticsEntity();
        List<CampaignsObjects> campaignsObjects = new ArrayList<>();
        Optional<OrganizationAnalyticsEntity> byId = organizationAnalyticsRepository.findById(ruleRequestRootModel.getOrganizationId());
        if(byId.isPresent()){
            organizationAnalyticsEntity = byId.get();
            campaignsObjects = byId.get().getAllCampaign();
            organizationAnalyticsEntity.setActiveCampaignCount(byId.get().getActiveCampaignCount() + 1);
            organizationAnalyticsEntity.setOrganizationTotalRequestCount(byId.get().getOrganizationTotalRequestCount());
            organizationAnalyticsEntity.setOrganizationTotalResponseCount(byId.get().getOrganizationTotalResponseCount());
            organizationAnalyticsEntity.setOrganizationMatchResponses(byId.get().getOrganizationMatchResponses());
        }
        CampaignsObjects campaignsObjects1 = new CampaignsObjects();
        campaignsObjects1.setCampaignId(ruleRequestRootModel.getCampaignId());
        campaignsObjects1.setCampaignName(ruleRequestRootModel.getCampaignName());
        campaignsObjects1.setCampaignDescription(ruleRequestRootModel.getCampaignDescription());
        campaignsObjects1.setStatus(ruleRequestRootModel.getStatus());
        campaignsObjects1.setCreatedDate(ruleRequestRootModel.getStartDateTime());
        campaignsObjects1.setEndDate(ruleRequestRootModel.getEndDateTime());
        campaignsObjects.add(campaignsObjects1);
        organizationAnalyticsEntity.setName(ruleRequestRootModel.getOrganizationId());
        organizationAnalyticsEntity.setAllCampaign(campaignsObjects);
        organizationAnalyticsRepository.save(organizationAnalyticsEntity);
    }


    //Find Root SegmentsObject With Multiple Child Rules From SegmentsObject ID
    public RuleRequestRootEntity findRootRuleById(String ruleId) {
        log.info("Find Root SegmentsObject By id " + ruleId);
        Optional<RuleRequestRootEntity> byId = rootRuleRepository.findById(ruleId);
        return byId.orElse(null);
    }

    //Remove Rules From Knowledge Base And Database From SegmentsObject ID
    public ServiceResponseDTO removeRuleFromKBAndDatabase(String segmentName) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase()" + segmentName);
        try {
            log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase() ruleEntity Name " + segmentName);
            internalKnowledgeBase.removeRule(packageName, segmentName);
            serviceResponseDTO.setDescription("removeRuleFromKBAndDatabase Success");
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
            return serviceResponseDTO;
        } catch (Exception exception) {
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("CampaignTemplateServiceImpl saveTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO updateCampaignStatus(String campaignId, String status) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        RuleRequestRootEntity ruleRequestRootEntity = findRootRuleById(campaignId);
        if ((RuleStatus.INACTIVE.toString().equals(status)) && ruleRequestRootEntity.getStatusEnum().equals(RuleStatus.ACTIVE)) {
            log.info("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase()" + campaignId);
//            for (SegmentsObject ruleEntity : ruleRequestRootEntity.getSegments ()) {
//                log.info ("LOG:: RuleServiceImpl removeRuleFromKBAndDatabase() ruleEntity Name " + ruleEntity.getSegmentName ());
//                internalKnowledgeBase.removeRule (packageName, ruleEntity.getSegmentName ());
//            }
            ruleRequestRootEntity.setStatusEnum(RuleStatus.valueOf(status));
            RuleRequestRootEntity save = rootRuleRepository.save(ruleRequestRootEntity);
            serviceResponseDTO.setData(save);
            serviceResponseDTO.setDescription("updateCampaignStatus Success");

        } else if ((RuleStatus.ACTIVE.toString().equals(status)) && (ruleRequestRootEntity.getStatusEnum().equals(RuleStatus.INACTIVE))) {
            ruleRequestRootEntity.setStatusEnum(RuleStatus.valueOf(status));
            droolService.feedKnowledge(ruleRequestRootEntity);
            RuleRequestRootEntity save = rootRuleRepository.save(ruleRequestRootEntity);
            serviceResponseDTO.setData(save);
            serviceResponseDTO.setDescription("updateCampaignStatus Success");
        }
        serviceResponseDTO.setMessage(STATUS_SUCCESS);
        serviceResponseDTO.setCode(STATUS_2000);
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
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
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        Optional<RuleRequestRootEntity> byId = rootRuleRepository.findById(id);
        if (byId.isPresent()) {
            serviceResponseDTO.setData(byId);
            serviceResponseDTO.setDescription("findByID Success");
        } else {
            serviceResponseDTO.setData(byId);
            serviceResponseDTO.setDescription("findByID Success No Data");
        }
        serviceResponseDTO.setMessage(STATUS_SUCCESS);
        serviceResponseDTO.setCode(STATUS_2000);
        serviceResponseDTO.setHttpStatus(STATUS_OK);
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
        rootRules.setStartDateTime(ruleRequestRootModel.getStartDateTime());
        rootRules.setEndDateTime(ruleRequestRootModel.getEndDateTime());
        if (ruleRequestRootModel.getOrganizationId() != null)
            rootRules.setOrganizationId(ruleRequestRootModel.getOrganizationId());
        rootRules.setPriority(ruleRequestRootModel.getPriority());
        if (ruleRequestRootModel.getChannelIds() != null) rootRules.setChannelIds(ruleRequestRootModel.getChannelIds());
        if (ruleRequestRootModel.getTags() != null) rootRules.setTags(ruleRequestRootModel.getTags());
        if (ruleRequestRootModel.getStatus() != null) rootRules.setStatusEnum(ruleRequestRootModel.getStatus());
        rootRules.setUpdatedDate(Date.from(now));
        return rootRules;
    }

    // Create Drool String to Create Multiple Rules
    public String createDrlString(ChannelContentObject channelContentObject, SegmentsObject segmentsObject, RuleRequestRootDTO ruleRequestRootModel) {
        boolean channelValidate = false;
        log.info("LOG:: DroolServiceImpl createDrlString() Inside Method ");

        String fact = segmentsObject.getSegmentRuleString();
        double priority = segmentsObject.getPriority();
        try {
            List<ChannelEntity> channelEntityList = channelRepository.findByChannelsId_OrganizationUuidEquals(ruleRequestRootModel.getOrganizationId());
            if (!channelEntityList.isEmpty()) {
                ChannelEntity channelObj = new ChannelEntity();
                for (ChannelEntity channelsObject : channelEntityList) {
                    if (channelsObject.getChannelUuid().equals(segmentsObject.getChannelId())) {
                        channelValidate = true;
                        channelObj = channelsObject;
                        break;
                    }
                }
                if (channelValidate) {
                    for (ExperiencesObject experiencesObject : segmentsObject.getExperiences()) {
                        log.info("LOG:: DroolServiceImpl createDrlString() priority: " + priority);
                        String metadata = "startDate > \"" + Utils.formatter.format(ruleRequestRootModel.getStartDateTime()) + "\" && endDate < \"" + Utils.formatter.format(ruleRequestRootModel.getEndDateTime()) + "\" && contextId.contains(" + "\"" + channelContentObject.getEntryId().toUpperCase() + "\"" + ") && (<channel>)";
                        log.info("LOG:: DroolServiceImpl createDrlString() metadata: " + metadata);
                        StringBuilder channel = new StringBuilder();

                        channel.append("channels.contains(").append("\"").append(channelObj.getChannelUuid().toUpperCase()).append("\"").append(")").append(" || ");

                        String metaString = metadata.replace("<channel>", channel.substring(0, channel.length() - 4));
                        PackageDescrBuilder pkg = DescrFactory.newPackage();
                        PackageDescrBuilder pkgDescBuilder = pkg.end();

                        Optional.ofNullable(segmentsObject.getSegmentName()).ifPresent(name -> segmentsObject.setSegmentName(saveSegmentNameGenerator(name)));
                        String[] segments = segmentsObject.getSegmentName().split("##\\$\\$\\$##");
                        String segmentName = segments[0];
                        pkgDescBuilder.newRule().name(segmentsObject.getSegmentName()).attribute("salience", priority + "").attribute("agenda-group", "\"" + ruleRequestRootModel.getOrganizationId().toUpperCase() + "\"").lhs().pattern("$user : User").constraint(fact + "").end().pattern("$meta : MetaData").constraint(metaString).end().end().rhs(
                                "response.addToResponse(" + "\"" + segmentName + "\",\"" + experiencesObject.getAbTestEnable() + "\"," + experiencesObject.getAbTestPercentage() + ",\"" + experiencesObject.getAbTestStartDate() + "\",\"" + experiencesObject.getAbTestEndDateTime() + "\"," + segmentsObject.getPriority() + ",\"" + channelContentObject.getEntryId().toUpperCase() + "\",\"" + channelContentObject.getVariantId() + "\");").end();

                        PackageDescr packageDescr = pkgDescBuilder.getDescr();
                        DrlDumper dumper = new DrlDumper();

                        String drlFile = dumper.dump(packageDescr);
                        log.info("LOG:: DroolServiceImpl createDrlString() Single drlFile :" + drlFile);
                        drlFile = drlFile.replaceFirst("package ", "");
                        if (segmentsObject.isTemplate()) {
                            saveTemplate(segmentsObject.getSegmentDescription(), segmentsObject.getSegmentName(), segmentsObject.getRuleObject());
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
        SegmentTemplateEntity segmentTemplateEntity = new SegmentTemplateEntity();
        segmentName = segmentName + "##$$$##" + firstUUID.timestamp();
        segmentTemplateEntity.setSegmentName(segmentName);
        segmentTemplateEntity.setSegmentationDescription(getSegmentationDescription);
        segmentTemplateEntity.setFact(fact);
        SegmentTemplateEntity save = segmentTemplateRepository.save(segmentTemplateEntity);
    }
    // Return Imports In Drool String
    public String getDroolImports() {
        log.info("LOG:: DroolServiceImpl createImports");
        PackageDescr pkg = DescrFactory.newPackage().name("com.cloudofgoods.xenia")
                .newImport().target("com.cloudofgoods.xenia.dto.caution.User").end()
                .newImport().target("org.springframework.util.CollectionUtils").end()
                .newImport().target("com.cloudofgoods.xenia.dto.caution.MetaData").end()
                .newGlobal().type("com.cloudofgoods.xenia.dto.D6nResponseModelDTO").identifier("response").end().getDescr();
        DrlDumper dumper = new DrlDumper();
        return dumper.dump(pkg);
    }
}
