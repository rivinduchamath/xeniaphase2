package com.cloudofgoods.xenia.controller;


import com.cloudofgoods.xenia.dto.OrganizationDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    @PostMapping(value = "${server.servlet.authorizeUser}")
    @Description("Check Authorize ")
    @Transactional
    public ServiceResponseDTO getAuthorize(@RequestBody OrganizationDTO organizationDTO) {
        log.info ("LOG::Inside the LoginController getAuthorize ");
        return null;
    }
}
