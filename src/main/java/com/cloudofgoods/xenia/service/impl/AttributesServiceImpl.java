package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.request.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.response.AttributeResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.AttributeEntity;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.models.composite.AttributesId;
import com.cloudofgoods.xenia.repository.AttributeRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AttributesService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class AttributesServiceImpl implements AttributesService {

    private final AttributeRepository attributeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveAttribute(AttributeRequestDTO attributesDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: AttributesServiceImpl saveAttribute Service Layer");
        try {
            if (attributesDTO.getAttributeUuid() != null) { // Update
                log.info("LOG:: AttributesServiceImpl saveAttribute Service Layer Update");
                    Optional<AttributeEntity> attributeEntity = attributeRepository.findByAttributeUuidEquals(attributesDTO.getAttributeUuid());
                    if (attributeEntity.isPresent()) {
                        if (!attributesDTO.getOrganizationUuid().equals("") && !attributesDTO.getAttributeName().equals("")){
                        attributeEntity.get().setAttributesId(new AttributesId(attributesDTO.getOrganizationUuid(), attributesDTO.getAttributeName()));}

                        attributeEntity.get().setAttributeUuid(attributesDTO.getAttributeUuid());
                        attributeEntity.get().setDisplayName(attributesDTO.getDisplayName());
                        attributeEntity.get().setType(attributesDTO.getType());
                        attributeEntity.get().setValues(attributesDTO.getValues());
                        attributeEntity.get().setTableName(attributesDTO.getTableName());

                        serviceResponseDTO.setData(responseAttribute(attributeRepository.save(attributeEntity.get())));
                        serviceResponseDTO.setDescription("Update Attribute Success");
                    }else {
                        serviceResponseDTO.setDescription("Update Attribute Fail Attribute Not Fount");
                    }
            } else {  // Save
                log.info("LOG:: AttributesServiceImpl saveAttribute Service Layer Save");

                    Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(attributesDTO.getOrganizationUuid());
                    if (organization.isPresent()) {
                        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                        AttributeEntity attributeEntity = new AttributeEntity();
                        attributeEntity.setAttributeUuid(timeBasedGenerator.generate() + "");
                        if (!attributesDTO.getOrganizationUuid().equals("") && !attributesDTO.getAttributeName().equals("")){
                            attributeEntity.setAttributesId(new AttributesId(
                                    attributesDTO.getOrganizationUuid(),
                                            attributesDTO.getAttributeName()));
                        }
                        attributeEntity.setDisplayName(attributesDTO.getDisplayName());
                        attributeEntity.setType(attributesDTO.getType());
                        attributeEntity.setValues(attributesDTO.getValues());
                        attributeEntity.setTableName(attributesDTO.getTableName());
                        serviceResponseDTO.setData(responseAttribute(attributeRepository.save(attributeEntity)));
                        serviceResponseDTO.setDescription("Save Attribute Success");
                    }else {
                        serviceResponseDTO.setDescription("Save Attribute Fail Organization Not Found");
                    }
            }
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl saveAttribute() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AttributesServiceImpl saveAttribute() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
       return serviceResponseDTO;
    }

    private AttributeResponseDTO responseAttribute(AttributeEntity save) {
        AttributeResponseDTO attributeResponseDTO = new AttributeResponseDTO();
        attributeResponseDTO.setAttributeName(save.getAttributesId().getAttributeName());
        attributeResponseDTO.setAttributeUuid(save.getAttributeUuid());
        attributeResponseDTO.setDisplayName(save.getDisplayName());
        attributeResponseDTO.setType(save.getType());
        attributeResponseDTO.setValues(save.getValues());
        attributeResponseDTO.setTableName(save.getTableName());
        return attributeResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getAttribute(GetRequestAttributeDTO requestAttributeDTO) {
        log.info("LOG:: AttributesServiceImpl getAttribute Service Layer");
        ServiceGetResponseDTO serviceResponseDTO = new ServiceGetResponseDTO();
        try {
            List<AttributeEntity> attributeEntities = attributeRepository.findAllByAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrAttributesIdOrganizationUuidEqualsAndTypeIn
                    (requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(), requestAttributeDTO.getOrganizationUuid(),requestAttributeDTO.getType(),
                            PageRequest.of(requestAttributeDTO.getPage(), requestAttributeDTO.getSize()));
            List<AttributeResponseDTO> attributeResponseDTOS = new ArrayList<>();
            for (AttributeEntity attribute  :  attributeEntities){
                attributeResponseDTOS.add( responseAttribute(attribute));
            }

           serviceResponseDTO.setCount(attributeRepository.countByAttributesIdOrganizationUuidEqualsAndAttributesIdAttributeNameStartingWithOrTypeIn
                    (requestAttributeDTO.getOrganizationUuid(), requestAttributeDTO.getAttributeName(), requestAttributeDTO.getType()));

            serviceResponseDTO.setData(attributeResponseDTOS);
            serviceResponseDTO.setDescription("Get Attribute Success");
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl getAttribute() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("AttributesServiceImpl getAttribute() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO deleteAttribute(String attributeUuid, String organizationUuid) {
        log.info("LOG:: AttributesServiceImpl deleteAttribute");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationEntityByUuidEquals(organizationUuid);
            if(organizationEntity.isPresent()){
                Optional<AttributeEntity> attributeEntity = attributeRepository.deleteAttributeByAttributeUuid(attributeUuid);
                if(attributeEntity.isPresent()){
                    serviceResponseDTO.setData(attributeEntity);
                    serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() Success");
                    serviceResponseDTO.setMessage("Success");
                }else {
                    serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() attribute Not Found");
                    serviceResponseDTO.setMessage("Success");
                }
            }else {
                serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() Organization Not Found");
                serviceResponseDTO.setMessage("Fail");
            }
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
        } catch (Exception exception) {
            log.info("LOG :: AttributesServiceImpl deleteAttribute() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("AttributesServiceImpl deleteAttribute() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
        }
        return serviceResponseDTO;
    }
}
