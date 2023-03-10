package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.request.ChannelsGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestChannelsDTO;
import com.cloudofgoods.xenia.dto.response.ChannelsResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.ChannelEntity;
import com.cloudofgoods.xenia.repository.ChannelRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.ChannelService;
import com.cloudofgoods.xenia.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.cloudofgoods.xenia.util.Utils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateChannel(ChannelDTO channelDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: ChannelServiceImpl saveOrUpdateChannel Service Layer");
        try {
            organizationRepository.findByUuidEquals(channelDTO.getOrganizationUuid()).ifPresentOrElse(org -> {
                ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO();
                if (NotEmptyOrNullValidator.isNotNullOrEmpty(channelDTO.getChannelUuid())) {
                    channelRepository.findByChannelUuidEquals(channelDTO.getChannelUuid())
                            .map(channel -> {
                                log.info("LOG:: ChannelServiceImpl saveOrUpdateChannel Update");
                                channel.getChannelsId().setChannelsName(channelDTO.getChannelName().toUpperCase());
                                channel.setType(channelDTO.getType().toUpperCase());
                                channel.setStatus(channelDTO.isStatus());
                                channel = channelRepository.save(channel);
                                channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName().toUpperCase());
                                channelsResponseDTO.setType(channel.getType().toUpperCase());
                                channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                                serviceResponseDTO.setData(channelsResponseDTO);
                                serviceResponseDTO.setDescription("Update Channel Success");
                                return Optional.of(channel);
                            })
                            .orElseGet(() -> {
                                serviceResponseDTO.setDescription("Update Fail Channel Not Found");
                                return Optional.empty();
                            });
                } else { // Save
                    log.info("LOG:: ChannelServiceImpl saveOrUpdateChannel Save");
                    ChannelEntity channel = channelRepository.save(new ChannelEntity(channelDTO.getOrganizationUuid(),
                            channelDTO.getChannelName().toUpperCase(), Utils.timeUuidGenerate(), channelDTO.getType().toUpperCase(), channelDTO.isStatus()));
                    channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName());
                    channelsResponseDTO.setType(channel.getType());
                    channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                    serviceResponseDTO.setData(channelsResponseDTO);
                    serviceResponseDTO.setDescription("Save Channel Success");
                }
            }, () -> {
                serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND);
            });
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl saveOrUpdateChannel() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("ChannelServiceImpl saveOrUpdateChannel() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getChannels(GetRequestChannelsDTO requestChannelsDTO) {
        log.info("LOG:: ChannelServiceImpl getChannels");
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            serviceGetResponseDTO.setCount(
                    channelRepository.countByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWithAndStatusEquals(requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase(),true));
            List<ChannelEntity> channels = requestChannelsDTO.isPagination() ?
                    channelRepository.findByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWithAndStatusEquals(
                            requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase(),true,
                            PageRequest.of(requestChannelsDTO.getPage(), requestChannelsDTO.getSize())) :
                    channelRepository.findByChannelsId_OrganizationUuidEqualsAndChannelsIdChannelsNameStartingWithAndStatusEquals(
                            requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase() ,true);
            List<ChannelsResponseDTO> channelsResponseDTOS = new ArrayList<>();
            channels.forEach(channel -> {
                ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO();
                channelsResponseDTO.setType(channel.getType());
                channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName());
                channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                channelsResponseDTO.setType(channel.getType());
                channelsResponseDTOS.add(channelsResponseDTO);
            });
            serviceGetResponseDTO.setData(channelsResponseDTOS);
            serviceGetResponseDTO.setDescription("Get Channel Success");
            serviceGetResponseDTO.setMessage(STATUS_SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);

        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl getChannels() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setMessage("ChannelServiceImpl getChannels() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(STATUS_FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

    @Override
    public ServiceResponseDTO activeOrInActiveChannels(String channelUuid, String organizationUuid, boolean status) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: ChannelServiceImpl deleteChannels Service Layer");
        try {
            channelRepository.findByChannelsId_OrganizationUuidEqualsAndChannelUuidEquals(organizationUuid, channelUuid)
                    .ifPresentOrElse(channelEntity -> {
                        channelEntity.setStatus(status);
                        serviceResponseDTO.setData(channelRepository.save(channelEntity));
                        serviceResponseDTO.setDescription(status ? "Activate" : "Deactivate" +" Channel Success");
                    }, () -> serviceResponseDTO.setDescription("Uuid is Not Found"));

            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl deleteChannels() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("ChannelServiceImpl deleteChannels() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getSingleChannelTable(ChannelsGetSingleDTO channelsGetSingleDTO) {
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            channelRepository.findByChannelsId_OrganizationUuidEqualsAndChannelUuidEquals(channelsGetSingleDTO.getOrganizationUuid(),
                            channelsGetSingleDTO.getChannelUuid())
                    .ifPresentOrElse(channelEntity -> {
                        serviceGetResponseDTO.setData(channelEntity);
                        serviceGetResponseDTO.setDescription("Get Channel Table Success");
                    }, () -> serviceGetResponseDTO.setDescription("Cannot Find Channel Table"));

            serviceGetResponseDTO.setMessage(STATUS_SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl getSingleChannelTable() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("ChannelServiceImpl getSingleChannelTable() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(STATUS_FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }
}
