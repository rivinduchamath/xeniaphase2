package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.AuthUser;
import com.cloudofgoods.xenia.models.CustomerObject;
import com.cloudofgoods.xenia.repository.UserRepository;
import com.cloudofgoods.xenia.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.cloudofgoods.xenia.util.Utils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateCustomer(AuthUser userDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            AuthUser authUser = new AuthUser ();
            if (userDTO.getId () != null) {
                log.info ("LOG:: UserServiceImpl saveOrUpdateCustomer Update");
                authUser.setId (userDTO.getId ());
                authUser.setUsername (userDTO.getUsername ());
                authUser.setHobby (userDTO.getHobby ());
                userRepository.save (authUser); // Update
                serviceResponseDTO.setData (authUser);
                serviceResponseDTO.setMessage ("Update Template Success");
            }else {
                log.info ("LOG:: UserServiceImpl saveOrUpdateCustomer Save");
                authUser.setUsername (userDTO.getUsername ());
                authUser.setHobby (userDTO.getHobby ());
                userRepository.save (authUser); // Save
                serviceResponseDTO.setData (authUser);
                serviceResponseDTO.setMessage ("Save Template Success");
            }
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: UserServiceImpl saveOrUpdateCustomer() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setMessage ("UserServiceImpl saveOrUpdateCustomer() exception " + exception.getMessage ());
            return serviceResponseDTO;
        }
    }


    @Override
    public ServiceResponseDTO getCustomer( int page, int size) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info ("LOG:: getCustomer");
        Instant now = Instant.now (); // 2023-10-15T11:22:11.000Z
        log.info (now.toString () + "  ");
        try {
            CustomerObject customerObject = new CustomerObject ();
//            List<CustomerEntityObject> stream = userRepository.findAllByEndDateTimeIsGreaterThanEqualAndSlotIdEquals1 (Date.from (now), PageRequest.of (page, size));
//            long count = userRepository.countAllByEndDateTimeIsGreaterThanEqual1 (Date.from (now));
//            customerObject.setCustomerEntityObjects (stream);
//            customerObject.setTotal (count);
            serviceResponseDTO.setData (customerObject);
            serviceResponseDTO.setMessage ("InitialPageServiceImpl getCampaignForInitialPage Success");
            serviceResponseDTO.setMessage (SUCCESS);
            serviceResponseDTO.setCode (STATUS_2000);

        } catch (Exception exception) {
            log.info ("LOG :: InitialPageServiceImpl getCampaignForInitialPage() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setMessage ("InitialPageServiceImpl getCampaignForInitialPage() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage (FAIL);
            serviceResponseDTO.setCode (STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus (STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO deleteCustomer(String customerId) {
        return null;
    }


}
