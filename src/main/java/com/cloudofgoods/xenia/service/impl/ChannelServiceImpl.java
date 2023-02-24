package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.GetRequestChannelsDTO;
import com.cloudofgoods.xenia.dto.response.ChannelsResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.entity.xenia.ChannelEntity;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateChannel(ChannelDTO channelDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: ChannelServiceImpl saveChannel Service Layer");
        try {
            ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO();
            if (channelDTO.getOrganizationUuid() != null) {
                Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(channelDTO.getOrganizationUuid());
                if (organization.isPresent()) {
                    if (channelDTO.getChannelUuid() != null) {
                        Optional<ChannelEntity> channelEntity = channelRepository.findByChannelUuidEquals(channelDTO.getChannelUuid());
                        if (channelEntity.isPresent()) { // Update
                            log.info("LOG:: ChannelServiceImpl saveOrUpdateChannel Update");

                            channelEntity.get().getChannelsId().setChannelsName(channelDTO.getChannelName().toUpperCase());
                            channelEntity.get().setType(channelDTO.getType().toUpperCase());
                            ChannelEntity channel = channelRepository.save(channelEntity.get());

                            channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName().toUpperCase());
                            channelsResponseDTO.setType(channel.getType().toUpperCase());
                            channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                            serviceResponseDTO.setData(channelsResponseDTO);
                            serviceResponseDTO.setDescription("Update Channel Success");
                            serviceResponseDTO.setMessage("Success");
                        } else {
                            serviceResponseDTO.setDescription("Update Fail Channel Not Found");
                            serviceResponseDTO.setMessage("Fail");
                        }
                    } else { //Save
                        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();

                        ChannelEntity channel = channelRepository.save(new ChannelEntity(channelDTO.getOrganizationUuid(), channelDTO.getChannelName().toUpperCase(), timeBasedGenerator.generate() + "", channelDTO.getType().toUpperCase()));
                        channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName());
                        channelsResponseDTO.setType(channel.getType());
                        channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                        serviceResponseDTO.setData(channelsResponseDTO);
                        serviceResponseDTO.setDescription("Save Channel Success");
                        serviceResponseDTO.setMessage("Success");
                    }
                }
            } else {
                serviceResponseDTO.setDescription("Organization Uuid Cannot Be Empty");
                serviceResponseDTO.setMessage("Fail");
            }
            serviceResponseDTO.setCode("2000");
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl saveChannel() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("ChannelServiceImpl saveChannel() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getChannels(GetRequestChannelsDTO requestChannelsDTO) {
        log.info("LOG:: ChannelServiceImpl getChannels");
        ServiceGetResponseDTO serviceResponseDTO = new ServiceGetResponseDTO();
        try {
            List<ChannelsResponseDTO> channelsResponseDTOS = new ArrayList<>();
            for (ChannelEntity channel : channelRepository.findByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWith(requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase(), PageRequest.of(requestChannelsDTO.getPage(), requestChannelsDTO.getSize()))) {

                ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO();
                channelsResponseDTO.setType(channel.getType());
                channelsResponseDTO.setChannelName(channel.getChannelsId().getChannelsName());
                channelsResponseDTO.setChannelUuid(channel.getChannelUuid());
                channelsResponseDTO.setType(channel.getType());
                channelsResponseDTOS.add(channelsResponseDTO);
            }
            serviceResponseDTO.setCount(channelRepository.countByChannelsIdOrganizationUuidEqualsAndChannelsIdChannelsNameStartingWith(requestChannelsDTO.getOrganizationUuid(), requestChannelsDTO.getChannelName().toUpperCase()));
            serviceResponseDTO.setData(channelsResponseDTOS);
            serviceResponseDTO.setDescription("Get Channel Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");

        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl getChannels() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("ChannelServiceImpl getChannels() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO deleteChannels(String channelUuId) {
        log.info("LOG:: ChannelServiceImpl deleteChannels Service Layer");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<ChannelEntity> channelEntity = channelRepository.deleteByChannelUuidEquals(channelUuId);
            if (channelEntity.isPresent()) {
                serviceResponseDTO.setData(channelEntity.get());
                serviceResponseDTO.setDescription("Delete Channel Success");
            } else {
                serviceResponseDTO.setDescription("Delete Uuid is Not Found");
            }
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl deleteChannels() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("ChannelServiceImpl deleteChannels() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }
}
