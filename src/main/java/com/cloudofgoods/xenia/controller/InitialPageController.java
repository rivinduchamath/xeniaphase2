package com.cloudofgoods.xenia.controller;


import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.InitialPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/d6n/initial")
@RequiredArgsConstructor
public class InitialPageController {

    private final InitialPageService initialPageService;

    @GetMapping(value = "${server.servlet.getCampaignsForInitialPageWithPagination}")
    public ServiceResponseDTO getCampaignsForInitialPageWithPagination(@RequestParam String slotId, @RequestParam int page, @RequestParam int size) {
        log.info ("LOG:: InitialPageController getCampaignForInitialPage ");
        if (page >= 0 && size > 0) {
            log.info ("LOG:: InitialPageController getCampaignForInitialPage !(page < 0 && size <= 0)");
            return initialPageService.getCampaignForInitialPage (slotId, page, size);
        }else {
            log.info ("LOG:: InitialPageController getCampaignForInitialPage Error With Request Param");
            ServiceResponseDTO responseDTO = new ServiceResponseDTO ();
            responseDTO.setMessage ("Success");
            responseDTO.setCode ("4000");
            responseDTO.setDescription ("REQUESTED RANGE NOT SATISFIABLE");
            responseDTO.setHttpStatus ("OK");
            return responseDTO;
        }
    }
}
