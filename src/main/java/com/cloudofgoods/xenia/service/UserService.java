package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.AuthUser;

public interface UserService {
    ServiceResponseDTO saveOrUpdateCustomer(AuthUser userDTO);
    ServiceResponseDTO getCustomer(int page, int size);
    ServiceResponseDTO deleteCustomer(String customerId);
}
