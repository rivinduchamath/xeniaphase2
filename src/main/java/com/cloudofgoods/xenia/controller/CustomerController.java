package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.AuthUser;
import com.cloudofgoods.xenia.service.UserService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/d6n/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;

    @PostMapping(value = "${server.servlet.saveUser}")
    @Description("Add User")
    @Transactional
    public ServiceResponseDTO saveUser(@RequestBody AuthUser userDTO) {
        log.info ("LOG::Inside the CustomerController saveUser ");
        return userService.saveOrUpdateCustomer (userDTO);
    }
    @GetMapping(value = "${server.servlet.getAttributes}")
    @Transactional
    @Description("Get Customers")
    public ServiceResponseDTO getCustomer(@RequestParam int page, @RequestParam int size) {
        log.info ("LOG::Inside the  CustomerController getCustomer ");
        if (page >= 0 && size > 0) {
            log.info ("LOG::  !(page < 0 && size <= 0)");
            return userService.getCustomer (page, size);
        }else {
            ServiceResponseDTO responseDTO = new ServiceResponseDTO ();
            responseDTO.setMessage ("Success");
            responseDTO.setCode ("4000");
            responseDTO.setDescription ("REQUESTED RANGE NOT SATISFIABLE");
            responseDTO.setHttpStatus ("OK");
            return responseDTO;
        }
    }
    @DeleteMapping(value = "${server.servlet.deleteAttribute}")
    @Transactional
    @Description("Delete Customer")
    public ServiceResponseDTO deleteCustomer(@RequestParam String customerId) {
        log.info("LOG::Inside the  CustomerController deleteCustomer ");
        return userService.deleteCustomer(customerId);
    }
}
