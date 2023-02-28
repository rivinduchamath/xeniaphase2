package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.request.ChannelsGetSingleDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestChannelsDTO;
import com.cloudofgoods.xenia.dto.response.ChannelsResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.ChannelEntity;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.repository.ChannelRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.ChannelService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(channelDTO.getOrganizationUuid());
            if (organization.isPresent()) {
                ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO();
                if (NotEmptyOrNullValidator.isNotNullOrEmpty(channelDTO.getChannelUuid())) {
                    Optional<ChannelEntity> channelEntity = channelRepository.findByChannelUuidEquals(channelDTO.getChannelUuid());
                    if (channelEntity.isPresent()) { // Update
                        log.info("LOG:: ChannelServiceImpl saveOrUpdateChannel Update");
                        ChannelEntity channel = channelEntity.get();
                        channel.getChannelsId().setChannelsName(channelDTO.getChannelName().toUpperCase());
                        channel.setType(channelDTO.getType().toUpperCase());
                        channel = channelRepository.save(channelEntity.get());
                        channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName().toUpperCase());
                        channelsResponseDTO.setType(channel.getType().toUpperCase());
                        channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                        serviceResponseDTO.setData(channelsResponseDTO);
                        serviceResponseDTO.setDescription("Update Channel Success");
                    } else {
                        serviceResponseDTO.setDescription("Update Fail Channel Not Found");
                        serviceResponseDTO.setMessage(FAIL);
                    }
                } else { //Save
                    NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                    ChannelEntity channel = channelRepository.save(new ChannelEntity(channelDTO.getOrganizationUuid(), channelDTO.getChannelName().toUpperCase(), timeBasedGenerator.generate() + "", channelDTO.getType().toUpperCase(), channelDTO.isStatus()));
                    channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName());
                    channelsResponseDTO.setType(channel.getType());
                    channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                    serviceResponseDTO.setData(channelsResponseDTO);
                    serviceResponseDTO.setDescription("Save Channel Success");
                }
            } else {
                serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND);
            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl saveChannel() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("ChannelServiceImpl saveChannel() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
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
            List<ChannelsResponseDTO> channelsResponseDTOS = new ArrayList<>();
            List<ChannelEntity> channels;
            if (requestChannelsDTO.isPagination()) {
                channels = channelRepository.findByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWith(
                        requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase(), PageRequest.of(requestChannelsDTO.getPage(), requestChannelsDTO.getSize()));
            } else {
                channels = channelRepository.findByChannelsId_OrganizationUuidEqualsAndChannelsIdChannelsNameStartingWith(
                        requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase());
            }
            channels.forEach(channel -> {
                ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO();
                channelsResponseDTO.setType(channel.getType());
                channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName());
                channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                channelsResponseDTO.setType(channel.getType());
                channelsResponseDTOS.add(channelsResponseDTO);
            });
            serviceGetResponseDTO.setCount(channelRepository.countByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWith(requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase()));
            serviceGetResponseDTO.setData(channelsResponseDTOS);
            serviceGetResponseDTO.setDescription("Get Channel Success");
            serviceGetResponseDTO.setMessage(SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);

        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl getChannels() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setMessage("ChannelServiceImpl getChannels() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

    @Override
    public ServiceResponseDTO deleteChannels(String channelUuid, String organizationUuid, boolean status) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: ChannelServiceImpl deleteChannels Service Layer");
        try {
            Optional<ChannelEntity> channelEntity = channelRepository.findByChannelsId_OrganizationUuidEqualsAndChannelUuidEquals(organizationUuid, channelUuid);
            if (channelEntity.isPresent()) {
                channelEntity.get().setStatus(status);
                serviceResponseDTO.setData(channelRepository.save(channelEntity.get()));
                serviceResponseDTO.setDescription("Delete Channel Success");
            } else {
                serviceResponseDTO.setDescription("Delete Uuid is Not Found");
            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl deleteChannels() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("ChannelServiceImpl deleteChannels() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getSingleChannelTable(ChannelsGetSingleDTO channelsGetSingleDTO) {
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            Optional<ChannelEntity> channelEntity = channelRepository.
                    findByChannelsId_OrganizationUuidEqualsAndChannelUuidEquals(channelsGetSingleDTO.getOrganizationUuid(), channelsGetSingleDTO.getChannelUuid());
            if (channelEntity.isPresent()) {
                serviceGetResponseDTO.setData(channelEntity.get());
                serviceGetResponseDTO.setDescription("Get Channel Table Success");
            } else {
                serviceGetResponseDTO.setDescription("Cannot Find Channel Table");
            }
            serviceGetResponseDTO.setMessage(SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl getSingleChannelTable() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("ChannelServiceImpl getSingleChannelTable() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }
}
