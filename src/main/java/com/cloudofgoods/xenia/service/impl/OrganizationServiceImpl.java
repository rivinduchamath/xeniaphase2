package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.OrganizationDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.OrganizationService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    @Override
    public ServiceResponseDTO saveOrUpdateOrganization(OrganizationDTO organizationDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            if (NotEmptyOrNullValidator.isNotNullOrEmpty(organizationDTO.getOrganizationUuid())) {
                organizationRepository.findByUuidEquals(organizationDTO.getOrganizationUuid())
                        .ifPresentOrElse(
                                organizationEntity -> {
                                    log.info("LOG:: OrganizationServiceImpl saveOrUpdateOrganization Update");
                                    organizationEntity.setName(organizationDTO.getName().toUpperCase());
                                    organizationEntity.setPassword(organizationDTO.getPassword());
                                    OrganizationEntity save = organizationRepository.save(organizationEntity);
                                    serviceResponseDTO.setData(save);
                                    serviceResponseDTO.setDescription("Update Organization Success");
                                },
                                () -> serviceResponseDTO.setDescription("Update Organization Not Found")
                        );

            } else {
                OrganizationEntity organizationEntity = new OrganizationEntity();
                log.info("LOG:: OrganizationServiceImpl saveOrUpdateOrganization Save");
                organizationEntity.setName(organizationDTO.getName().toUpperCase());
                NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                UUID firstUUID = timeBasedGenerator.generate();
                organizationEntity.setUuid(firstUUID + "");
                organizationEntity.setPassword(organizationDTO.getPassword());
                OrganizationEntity save = organizationRepository.save(organizationEntity);// Save
                serviceResponseDTO.setData(save);
                serviceResponseDTO.setDescription("Save Organization Success");
            }
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
        } catch (Exception exception) {
            log.info("LOG :: OrganizationServiceImpl saveOrUpdateOrganization() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("OrganizationServiceImpl saveOrUpdateOrganization() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getOrganization() {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            serviceResponseDTO.setData(organizationRepository.findAll());
            serviceResponseDTO.setDescription("Get Organization Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
        } catch (Exception exception) {
            log.info("LOG :: OrganizationServiceImpl getOrganization() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("OrganizationServiceImpl getOrganization() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
        return serviceResponseDTO;
    }
}
