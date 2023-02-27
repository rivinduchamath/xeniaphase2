package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.ChannelDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestChannelsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.ChannelService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/d6n/channel")
@Slf4j
@RequiredArgsConstructor
public class ChannelsController {
    private final ChannelService channelService;
    @PostMapping(value = "${server.servlet.saveChannels}")
    @Transactional
    @Description("Add ChannelsEntity")
    public ServiceResponseDTO saveChannels(@RequestBody ChannelDTO channelDTO) {
        log.info ("LOG::Inside the ChannelsController saveChannels ");
        return channelService.saveOrUpdateChannel(channelDTO);
    }
    @PostMapping(value = "${server.servlet.getChannels}")
    @Transactional
    @Description("Get ChannelsEntity")
    public ServiceGetResponseDTO getChannels(@RequestBody GetRequestChannelsDTO requestChannelsDTO) {
        log.info ("LOG::Inside the ChannelsController getChannels ");
        return channelService.getChannels (requestChannelsDTO);
    }
    @DeleteMapping(value = "${server.servlet.deleteChannels}")
    @Transactional
    @Description("Delete ChannelsEntity")
    public ServiceResponseDTO deleteChannels(@RequestParam String channelUuId) {
        log.info ("LOG::Inside the ChannelsController deleteChannels ");
        return channelService.deleteChannels (channelUuId);
    }
}