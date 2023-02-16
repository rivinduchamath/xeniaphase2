package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.channel.ChannelsResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.models.composite.ChannelsId;
import com.cloudofgoods.xenia.entity.xenia.ChannelsEntity;
import com.cloudofgoods.xenia.repository.ChannelRepository;
import com.cloudofgoods.xenia.service.ChannelService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public ServiceResponseDTO saveChannel(ChannelDTO channelDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        log.info ("LOG:: ChannelServiceImpl saveChannel");
        try {
            ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO ();

            ChannelsEntity channelsEntity = new ChannelsEntity ();
            if (channelDTO.getUuid () != null) { // Update
                log.info ("LOG:: ChannelServiceImpl saveChannel Update");
                try {
                    channelsEntity = channelRepository.findByUuid (channelDTO.getUuid ());
                } catch (Exception exception) {
                    serviceResponseDTO.setDescription ("ChannelServiceImpl saveChannel Find By Uuid Not Found");
                    serviceResponseDTO.setMessage ("Success");
                    serviceResponseDTO.setCode ("5000");
                    serviceResponseDTO.setHttpStatus ("OK");
                }
                channelsEntity.setUuid (channelsEntity.getUuid ());
                channelsEntity.setChannelsId (new ChannelsId (channelDTO.getChannelsIdDTO ().getOrganization (), channelDTO.getChannelsIdDTO ().getChannelsName ()));
                ChannelsEntity save = channelRepository.save (channelsEntity);// Update
                channelsResponseDTO.setChannelsName (save.getChannelsId ().getChannelsName ());
                channelsResponseDTO.setUuid (save.getUuid ());
                serviceResponseDTO.setData (channelsResponseDTO);
                serviceResponseDTO.setDescription ("Update Channel Success");
                serviceResponseDTO.setMessage ("Success");
                serviceResponseDTO.setCode ("2000");
                serviceResponseDTO.setHttpStatus ("OK");
            }else {
                log.info ("LOG:: ChannelServiceImpl saveChannel Save");
                channelsEntity.setChannelsId (new ChannelsId (channelDTO.getChannelsIdDTO ().getOrganization (), channelDTO.getChannelsIdDTO ().getChannelsName ()));

                NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator ();
                UUID firstUUID = timeBasedGenerator.generate ();
                channelsEntity.setUuid (firstUUID.timestamp () + "");
                ChannelsEntity save = channelRepository.save (channelsEntity);// Save
                channelsResponseDTO.setChannelsName (save.getChannelsId ().getChannelsName ());
                channelsResponseDTO.setUuid (save.getUuid ());
                serviceResponseDTO.setData (channelsResponseDTO);
                serviceResponseDTO.setDescription ("Save Channel Success");
                serviceResponseDTO.setMessage ("Success");
                serviceResponseDTO.setCode ("2000");
                serviceResponseDTO.setHttpStatus ("OK");
            }
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: ChannelServiceImpl saveChannel() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setDescription ("ChannelServiceImpl saveChannel() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceGetResponseDTO getChannels(int page, int size, String channelName, String organization) {
        log.info ("LOG:: ChannelServiceImpl getChannels");
        ServiceGetResponseDTO serviceResponseDTO = new ServiceGetResponseDTO ();
        try {
            Page <ChannelsEntity> channels = channelRepository.findAllByChannelsIdChannelsNameStartingWithAndChannelsIdOrganization (channelName, organization, PageRequest.of (page, size));
            List <ChannelsResponseDTO> content = new ArrayList <> ();
            for (ChannelsEntity channelsEntity1 : channels.getContent ()) {
                ChannelsResponseDTO channelsResponseDTO = new ChannelsResponseDTO ();
                channelsResponseDTO.setChannelsName (channelsEntity1.getChannelsId ().getChannelsName ());
                channelsResponseDTO.setUuid (channelsEntity1.getUuid ());
                content.add (channelsResponseDTO);
            }
            serviceResponseDTO.setCount (channels.getTotalElements ());
            serviceResponseDTO.setData (content);
            serviceResponseDTO.setDescription ("Get Channel Success");
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: ChannelServiceImpl getChannels() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setMessage ("ChannelServiceImpl getChannels() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO deleteChannels(String channelUuId) {
        log.info ("LOG:: ChannelServiceImpl deleteChannels");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        try {
            ChannelsEntity channelsEntity;
            channelsEntity = channelRepository.findByUuid (channelUuId);
            if (channelsEntity == null) {
                serviceResponseDTO.setDescription ("Delete Uuid is Not Found");
            }else {
                channelRepository.deleteByUuid (channelUuId);
                serviceResponseDTO.setDescription ("Delete Channel Success");
            }
            serviceResponseDTO.setData (channelUuId);
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: ChannelServiceImpl deleteChannels() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setMessage ("ChannelServiceImpl deleteChannels() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }
}
