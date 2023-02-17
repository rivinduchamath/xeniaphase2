package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.channel.ChannelsResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.ChannelsObjects;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.models.AttributesObject;
import com.cloudofgoods.xenia.models.composite.ChannelsId;
import com.cloudofgoods.xenia.repository.ChannelRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.ChannelService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveChannel(ChannelDTO channelDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: ChannelServiceImpl saveChannel");
        try {
            ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO();
            OrganizationEntity organization = new OrganizationEntity();

            if (channelDTO.getChannelUuid() != null) { // Update
                log.info("LOG:: ChannelServiceImpl saveChannel Update");
                try {
                    organization = organizationRepository.findByChannelsObjectsUuid(channelDTO.getChannelUuid());
                } catch (Exception exception) {
                    serviceResponseDTO.setDescription("ChannelServiceImpl saveChannel Find By Uuid Not Found");
                    serviceResponseDTO.setMessage("Success");
                    serviceResponseDTO.setCode("5000");
                    serviceResponseDTO.setHttpStatus("OK");
                }
                List<ChannelsObjects> channelsObjectsList = organization.getChannelsObjects();
                channelsObjectsList.stream().filter(channelsObjects -> channelsObjects.getUuid().equals(channelDTO.getChannelUuid())).forEach(channelsObjects -> {
                    channelsObjects.setChannelsName(channelDTO.getChannelsIdDTO().getChannelsName());
                    channelsObjects.setUuid(channelDTO.getChannelUuid());

                });

                organization.setChannelsObjects(channelsObjectsList);
                organizationRepository.save(organization);


                channelsResponseDTO.setUuid(channelDTO.getChannelUuid());
                serviceResponseDTO.setData(channelsResponseDTO);
                serviceResponseDTO.setDescription("Update Channel Success");
                serviceResponseDTO.setMessage("Success");
                serviceResponseDTO.setCode("2000");
                serviceResponseDTO.setHttpStatus("OK");
            } else {
                log.info("LOG:: ChannelServiceImpl saveChannel Save");
                try {
                    organization = organizationRepository.findByUuid(channelDTO.getChannelsIdDTO().getOrganizationUuid());
                } catch (Exception exception) {
                    serviceResponseDTO.setDescription("Find By Uuid Not Found");
                    serviceResponseDTO.setMessage("Success");
                    serviceResponseDTO.setCode("5000");
                    serviceResponseDTO.setHttpStatus("OK");
                    return serviceResponseDTO;
                }
                NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                String firstUUID = timeBasedGenerator.generate() + "";
                List<ChannelsObjects> existingChannels = organization.getChannelsObjects();
                existingChannels = Optional.ofNullable(existingChannels).map(ArrayList::new).orElse(new ArrayList<>());

                existingChannels.add(new ChannelsObjects(channelDTO.getChannelsIdDTO().getChannelsName(), firstUUID));

                organization.setChannelsObjects(existingChannels);
                organizationRepository.save(organization);// Update

                channelsResponseDTO.setChannelsName(channelDTO.getChannelsIdDTO().getChannelsName());
                channelsResponseDTO.setUuid(firstUUID);

                serviceResponseDTO.setData(channelsResponseDTO);

                serviceResponseDTO.setDescription("Save Channel Success");
                serviceResponseDTO.setMessage("Success");
                serviceResponseDTO.setCode("2000");
                serviceResponseDTO.setHttpStatus("OK");
            }
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl saveChannel() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("ChannelServiceImpl saveChannel() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceGetResponseDTO getChannels(int start, int end, String channelName, String organization) {
        log.info("LOG:: ChannelServiceImpl getChannels");
        ServiceGetResponseDTO serviceResponseDTO = new ServiceGetResponseDTO();
        try {
            OrganizationEntity organizationEntities = organizationRepository.findByUuidEqualsAndChannelsObjectsChannelsNameEquals (organization, channelName);
            if (organizationEntities != null) {
                List <ChannelsObjects> content = new ArrayList <> (organizationEntities.getChannelsObjects ());
                List<ChannelsObjects> filteredChannels = content.stream()
                        .filter(channel -> channel.getChannelsName().toLowerCase().startsWith(channelName.toLowerCase()))
                        .collect(Collectors.toList());

                int count = filteredChannels.size ();
                int pageStart = Math.min (start, count);
                int pageEnd = Math.min (end, count);

                List <ChannelsObjects> pageContent = filteredChannels.subList (pageStart, pageEnd);
                Pageable pageable = PageRequest.of (pageStart, pageContent.size ());
                Page <ChannelsObjects> page2 = new PageImpl<>(pageContent, pageable, count);
                serviceResponseDTO.setData (page2.getContent ());
                serviceResponseDTO.setCount (count);
            }
            serviceResponseDTO.setDescription("Get Channel Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl getChannels() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("ChannelServiceImpl getChannels() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO deleteChannels(String channelUuId) {
        log.info("LOG:: ChannelServiceImpl deleteChannels");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            ChannelsObjects channelsObjects;
            channelsObjects = channelRepository.findByUuid(channelUuId);
            if (channelsObjects == null) {
                serviceResponseDTO.setDescription("Delete Uuid is Not Found");
            } else {
                channelRepository.deleteByUuid(channelUuId);
                serviceResponseDTO.setDescription("Delete Channel Success");
            }
            serviceResponseDTO.setData(channelUuId);
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: ChannelServiceImpl deleteChannels() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setMessage("ChannelServiceImpl deleteChannels() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }
}
