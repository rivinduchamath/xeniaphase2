package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.D6nDTO;
import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.AuthUser;
import com.cloudofgoods.xenia.repository.UserRepository;
import com.cloudofgoods.xenia.service.D6nService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/d6n")
@RequiredArgsConstructor
@Validated
public class D6nController {
    private final D6nService d6nService;
    private final UserRepository users;

    @Description("Create This API For makeDecision (Personalise)")
    @PostMapping(value = "${server.servlet.generateVariant}")
    public ServiceResponseDTO generateVariant(@RequestBody @Valid D6nDTO d6nDTO) {
        log.info("LOG:: D6nController makeDecision calling " + " user " + d6nDTO.getUserEmail() + "OrganizationEntity" + d6nDTO.getOrganization() + " channels " + d6nDTO.getChannels().toString() + " slot " + d6nDTO.getSlot().toString());
        AuthUser user = null;
        User userDTO = new User();

        if (d6nDTO.getUserEmail() != null) {
            user = this.users.findByUsername(d6nDTO.getUserEmail());
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("age", user.getAge());
            linkedHashMap.put("hobby", user.getHobby());
            linkedHashMap.put("roles", user.getRoles());
            linkedHashMap.put("country", user.getCountry());
            linkedHashMap.put("religion", user.getReligion());
            linkedHashMap.put("maritalStatus", user.getMaritalStatus());
            userDTO.setUserData(linkedHashMap);
        } else {

        }
        return d6nService.makeDecision(d6nDTO.getNumberOfResponseFrom(), d6nDTO.getNumberOfResponse(), d6nDTO.getUserEmail(), userDTO, d6nDTO.getChannels(), d6nDTO.getSlot(), d6nDTO.getOrganization());

    }
}