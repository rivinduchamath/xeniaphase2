package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.response.AttributeResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.models.AttributesObject;
import com.cloudofgoods.xenia.repository.AttributeRepository;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.service.AttributesService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AttributesServiceImpl implements AttributesService {

    private final AttributeRepository attributeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ServiceResponseDTO saveAttribute(AttributeRequestDTO attributesDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        log.info ("LOG:: AttributesServiceImpl saveAttribute");
        try {
            AttributeResponseDTO attributeResponseDTO = new AttributeResponseDTO ();

            if (attributesDTO.getUuid () != null) { // Update
                OrganizationEntity organization = new OrganizationEntity ();
                try {
                    organization = organizationRepository.findByAttributesObjectUuid (attributesDTO.getUuid ());
                } catch (Exception exception) {
                    serviceResponseDTO.setDescription ("Find By Uuid Not Found");
                    serviceResponseDTO.setMessage ("Success");
                    serviceResponseDTO.setCode ("5000");
                    serviceResponseDTO.setHttpStatus ("OK");
                }
                log.info ("LOG:: AttributesServiceImpl saveAttribute Update");
                List <AttributesObject> attributesObjectList = organization.getAttributesObject ();
                attributesObjectList.stream ().filter (attributesObject -> attributesObject.getUuid ().equals (attributesDTO.getUuid ())).forEach (attributesObject -> {
                    attributesObject.setAttributeName (attributesDTO.getAttributeIdDTO ().getAttributeName ());
                    attributesObject.setUuid (attributesDTO.getUuid ());
                    attributesObject.setDisplayName (attributesDTO.getDisplayName ());
                    attributesObject.setType (attributesDTO.getType ());
                    attributesObject.setValues (attributesDTO.getValues ());
                });
                organization.setAttributesObject (attributesObjectList);
                organizationRepository.save (organization);// Update
                attributeResponseDTO.setAttributeName (attributesDTO.getAttributeIdDTO ().getAttributeName ());
                attributeResponseDTO.setUuid (attributesDTO.getUuid ());
                attributeResponseDTO.setDisplayName (attributesDTO.getDisplayName ());
                attributeResponseDTO.setType (attributesDTO.getType ());
                attributeResponseDTO.setValues (attributesDTO.getValues ());
                serviceResponseDTO.setData (attributeResponseDTO);
                serviceResponseDTO.setDescription ("Save or Update Attribute  Attribute Success");
                serviceResponseDTO.setMessage ("Success");
                serviceResponseDTO.setCode ("2000");
                serviceResponseDTO.setHttpStatus ("OK");
            }else {  // Save
                log.info("LOG:: AttributesServiceImpl saveAttribute Save");

                Optional<OrganizationEntity> organization;
                try {
                    organization = organizationRepository.findByUuid(attributesDTO.getAttributeIdDTO().getOrganizationUuid());
                } catch (Exception exception) {
                    serviceResponseDTO.setDescription("Find By Uuid Not Found");
                    serviceResponseDTO.setMessage("Success");
                    serviceResponseDTO.setCode("5000");
                    serviceResponseDTO.setHttpStatus("OK");
                    return serviceResponseDTO;
                }
                log.info("LOG:: AttributesServiceImpl saveAttribute Save");
                if (organization.isPresent()) {
                    NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
                    String firstUUID = timeBasedGenerator.generate() + "";
                    List<AttributesObject> existingAttributes = organization.get().getAttributesObject();
                    existingAttributes = Optional.ofNullable(existingAttributes).map(ArrayList::new).orElse(new ArrayList<>());

                    existingAttributes.add(new AttributesObject(attributesDTO.getAttributeIdDTO().getAttributeName(), attributesDTO.getDisplayName(), attributesDTO.getType(), attributesDTO.getValues(), firstUUID));

                    organization.get().setAttributesObject(existingAttributes);
                    organizationRepository.save(organization.get());// Update

                    attributeResponseDTO.setAttributeName(attributesDTO.getAttributeIdDTO().getAttributeName());
                    attributeResponseDTO.setUuid(firstUUID);
                    attributeResponseDTO.setDisplayName(attributesDTO.getDisplayName());
                    attributeResponseDTO.setType(attributesDTO.getType());
                    attributeResponseDTO.setValues(attributesDTO.getValues());
                    serviceResponseDTO.setData(attributeResponseDTO);
                    serviceResponseDTO.setDescription("Save Attribute Success");
                    serviceResponseDTO.setMessage("Success");
                    serviceResponseDTO.setCode("2000");
                    serviceResponseDTO.setHttpStatus("OK");

                return serviceResponseDTO;
            }}
        } catch (Exception exception) {
            log.info ("LOG :: AttributesServiceImpl saveAttribute() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            exception.getStackTrace ();
            serviceResponseDTO.setDescription ("AttributesServiceImpl saveAttribute() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
        return null;
    }

    @Override
    public ServiceGetResponseDTO getAttribute(int start, int end, String organization, String attributeName, List <String> type) {
        log.info ("LOG:: AttributesServiceImpl getAttribute");
        ServiceGetResponseDTO serviceResponseDTO = new ServiceGetResponseDTO ();
        try {
            OrganizationEntity organizationEntities = organizationRepository.findByUuidEqualsAndAttributesObjectAttributeNameStartingWithOrAttributesObjectTypeIn (organization, attributeName, type);
            if (organizationEntities != null) {
                List <AttributesObject> content = new ArrayList <> (organizationEntities.getAttributesObject ());
                List <AttributesObject> filteredAttributes = content.stream ().filter (attributesObject -> attributesObject.getAttributeName ().startsWith (attributeName) || !Collections.disjoint (Collections.singleton (attributesObject.getType ()), type)).collect (Collectors.toList ());

                int count = filteredAttributes.size ();
                int pageStart = Math.min (start, count);
                int pageEnd = Math.min (end, count);

                List <AttributesObject> pageContent = filteredAttributes.subList (pageStart, pageEnd);
                Pageable pageable = PageRequest.of (pageStart, pageContent.size ());
                Page <AttributesObject> page = new PageImpl <> (pageContent, pageable, count);
                serviceResponseDTO.setData (page.getContent ());
                serviceResponseDTO.setCount (count);
            }
            serviceResponseDTO.setDescription ("Get Attribute Success");
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: AttributesServiceImpl getAttribute() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setDescription ("AttributesServiceImpl getAttribute() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }
    @Override
    public ServiceResponseDTO deleteAttribute(String attributeUuid, String organizationUuid) {
        log.info ("LOG:: AttributesServiceImpl deleteAttribute");
        log.info ("LOG :: AttributesServiceImpl deleteAttribute()");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        OrganizationEntity organizationEntity;
        try {
            organizationEntity = organizationRepository.findOrganizationEntityByUuidEquals (organizationUuid);
            Optional.ofNullable (organizationEntity).ifPresentOrElse (__ -> {
                List<AttributesObject> attributesObjects = organizationEntity.getAttributesObject();
                attributesObjects.removeIf(attributesObjectLoop -> attributesObjectLoop.getUuid().equals(attributeUuid));
                organizationEntity.setAttributesObject(attributesObjects);
                organizationRepository.save(organizationEntity);
                serviceResponseDTO.setDescription("Success");
            }, () -> serviceResponseDTO.setDescription ("Delete Uuid is Not Found"));
            serviceResponseDTO.setData (attributeUuid);
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: AttributesServiceImpl deleteAttribute() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            exception.printStackTrace ();
            serviceResponseDTO.setDescription ("AttributesServiceImpl deleteAttribute() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }
}
