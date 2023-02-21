package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

import java.util.List;

public interface D6nService {

    ServiceResponseDTO makeDecision(String userEmail, User user, List<String> channel, List<String> slot, String organization);
}
