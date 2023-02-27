package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.TagsDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestTagsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.dto.response.TagsResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.entity.xenia.TagsEntity;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.repository.TagsRepository;
import com.cloudofgoods.xenia.service.TagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final OrganizationRepository organizationRepository;
    private final TagsRepository tagsRepository;

    @Override
    public ServiceResponseDTO saveOrUpdateTags(TagsDTO tagsRequestDTO) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: TagsServiceImpl saveOrUpdateTags Service Layer");
        try {
            TagsDTO tagsResponseDTO = new TagsDTO();
            if (tagsRequestDTO.getOrganizationUuid() != null) {
                Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(tagsRequestDTO.getOrganizationUuid());
                if (organization.isPresent()) {
                    TagsEntity tags = tagsRepository.save(new TagsEntity(tagsRequestDTO.getTagsName().toUpperCase(), tagsRequestDTO.getOrganizationUuid()));
                    tagsResponseDTO.setTagsName(tags.getTagsId().getTagsName());
                    tagsResponseDTO.setOrganizationUuid(tags.getTagsId().getOrganizationUuid());
                    serviceResponseDTO.setData(tagsResponseDTO);
                    serviceResponseDTO.setDescription("Save/Update Tags Success");
                    serviceResponseDTO.setMessage("Success");
                }
            } else {
                serviceResponseDTO.setDescription("Organization Uuid Cannot Be Empty");
                serviceResponseDTO.setMessage("Fail");
            }
            serviceResponseDTO.setCode("2000");
        } catch (Exception exception) {
            log.info("LOG :: TagsServiceImpl saveOrUpdateTags() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("TagsServiceImpl saveOrUpdateTags() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }
        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }

    private TagsResponseDTO tagsResponseDTO(TagsEntity save) {
        TagsResponseDTO tagsResponseDTO = new TagsResponseDTO();
        tagsResponseDTO.setOrganizationUuid(save.getTagsId().getOrganizationUuid());
        tagsResponseDTO.setTagsName(save.getTagsId().getTagsName());
        return tagsResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getTags(GetRequestTagsDTO getRequestTagsDTO) {
        log.info("LOG:: TagsServiceImpl getTags Service Layer");
        ServiceGetResponseDTO serviceResponseDTO = new ServiceGetResponseDTO();
        try {
            List<TagsEntity> tagsEntities;
            if (getRequestTagsDTO.getOrganizationUuid() != null) {
                if (getRequestTagsDTO.getTagsName() != null) {
                    tagsEntities = tagsRepository.findAllByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(getRequestTagsDTO.getOrganizationUuid(), getRequestTagsDTO.getTagsName(), PageRequest.of(getRequestTagsDTO.getPage(), getRequestTagsDTO.getSize()));
                    serviceResponseDTO.setCount(tagsRepository.countByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(getRequestTagsDTO.getOrganizationUuid(), getRequestTagsDTO.getTagsName()));

                } else {
                    tagsEntities = tagsRepository.findAllByTagsIdOrganizationUuidEquals(getRequestTagsDTO.getOrganizationUuid(), PageRequest.of(getRequestTagsDTO.getPage(), getRequestTagsDTO.getSize()));
                    serviceResponseDTO.setCount(tagsRepository.countByTagsIdOrganizationUuidEquals(getRequestTagsDTO.getOrganizationUuid()));
                }

                List<TagsResponseDTO> tagsResponseDTOS = new ArrayList<>();
                for (TagsEntity tags : tagsEntities) {
                    tagsResponseDTOS.add(tagsResponseDTO(tags));
                }
                serviceResponseDTO.setData(tagsResponseDTOS);
                serviceResponseDTO.setDescription("Get Tags Success");
                serviceResponseDTO.setMessage("Success");
                serviceResponseDTO.setCode("2000");
            }
        } catch (Exception exception) {
            log.info("LOG :: TagsServiceImpl getTags() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("TagsServiceImpl getTags() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
        }

        serviceResponseDTO.setHttpStatus("OK");
        return serviceResponseDTO;
    }
}
