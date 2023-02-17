package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.OrganizationDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.OrganizationService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateOrganization(OrganizationDTO organizationDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        try {
            OrganizationEntity organizationEntity = new OrganizationEntity ();
            if (organizationDTO.getUuid () != null) {
                organizationEntity = organizationRepository.findByUuid (organizationDTO.getUuid ());
                log.info ("LOG:: OrganizationServiceImpl saveOrUpdateOrganization Update");
                organizationEntity.setName (organizationDTO.getName ());
                organizationEntity.setPassword (organizationDTO.getPassword ());
                organizationEntity.setAttributesObject (organizationDTO.getAttributesObject ());
                OrganizationEntity save = organizationRepository.save (organizationEntity);// Update
                serviceResponseDTO.setData (save);
                serviceResponseDTO.setDescription  ("Update Template Success");
                serviceResponseDTO.setMessage ("Success");
                serviceResponseDTO.setCode ("2000");
                serviceResponseDTO.setHttpStatus ("OK");
            }else {
                log.info ("LOG:: CampaignTemplateServiceImpl saveTemplate Save");
                organizationEntity.setName (organizationDTO.getName ());
                NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                UUID firstUUID = timeBasedGenerator.generate();
                organizationEntity.setUuid (firstUUID+"");
                organizationEntity.setPassword (organizationDTO.getPassword ());
                organizationEntity.setAttributesObject (organizationDTO.getAttributesObject ());
                organizationRepository.save (organizationEntity); // Save
                serviceResponseDTO.setData (organizationEntity);
                serviceResponseDTO.setDescription  ("Save Template Success");
                serviceResponseDTO.setMessage ("Success");
                serviceResponseDTO.setCode ("2000");
                serviceResponseDTO.setHttpStatus ("OK");
            }
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: OrganizationServiceImpl saveOrUpdateOrganization() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setDescription  ("OrganizationServiceImpl saveOrUpdateOrganization() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO getOrganization(String organizationId) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        try {

        } catch (Exception exception) {
            log.info ("LOG :: OrganizationServiceImpl getOrganization() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setDescription ("OrganizationServiceImpl getOrganization() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");

            return serviceResponseDTO;
        }
        return null;
    }
}
