package com.cloudofgoods.xenia.controller;


import com.cloudofgoods.xenia.dto.request.InitialPageRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.InitialPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/d6n/initial")
@RequiredArgsConstructor
public class InitialPageController {

    private final InitialPageService initialPageService;

    @PostMapping(value = "${server.servlet.getCampaignsForInitialPageWithPagination}")
    public ServiceResponseDTO getCampaignsForInitialPageWithPagination(@RequestBody InitialPageRequestDTO initialPageRequestDTO) {
        log.info("LOG:: InitialPageController getCampaignForInitialPage ");
        return initialPageService.getCampaignForInitialPage(initialPageRequestDTO);

    }
}
