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

import static com.cloudofgoods.xenia.util.Utils.*;

@Service
@Slf4j
@AllArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final OrganizationRepository organizationRepository;
    private final TagsRepository tagsRepository;
    private final ServiceResponseDTO serviceResponseDTO;
    private final ServiceGetResponseDTO serviceGetResponseDTO;

    @Override
    public ServiceResponseDTO saveOrUpdateTags(TagsDTO tagsRequestDTO) {
        log.info("LOG:: TagsServiceImpl saveOrUpdateTags Service Layer");
        try {
            TagsDTO tagsResponseDTO = new TagsDTO();

            Optional<OrganizationEntity> organization = organizationRepository.findByUuidEquals(tagsRequestDTO.getOrganizationUuid());
            if (organization.isPresent()) {
                TagsEntity tags = tagsRepository.save(new TagsEntity(tagsRequestDTO.getTagsName().toUpperCase(), tagsRequestDTO.getOrganizationUuid()));
                tagsResponseDTO.setTagsName(tags.getTagsId().getTagsName());
                tagsResponseDTO.setOrganizationUuid(tags.getTagsId().getOrganizationUuid());
                serviceResponseDTO.setData(tagsResponseDTO);
                serviceResponseDTO.setDescription("Save/Update Tags Success");
            } else {
                serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND);

            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: TagsServiceImpl saveOrUpdateTags() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("TagsServiceImpl saveOrUpdateTags() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
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
        try {
            List<TagsEntity> tagsEntities;
            if (getRequestTagsDTO.getOrganizationUuid() != null) {
                if (getRequestTagsDTO.getTagsName() != null) {
                    tagsEntities = tagsRepository.findAllByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(getRequestTagsDTO.getOrganizationUuid(), getRequestTagsDTO.getTagsName(), PageRequest.of(getRequestTagsDTO.getPage(), getRequestTagsDTO.getSize()));
                    serviceGetResponseDTO.setCount(tagsRepository.countByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(getRequestTagsDTO.getOrganizationUuid(), getRequestTagsDTO.getTagsName()));
                } else {
                    tagsEntities = tagsRepository.findAllByTagsIdOrganizationUuidEquals(getRequestTagsDTO.getOrganizationUuid(), PageRequest.of(getRequestTagsDTO.getPage(), getRequestTagsDTO.getSize()));
                    serviceGetResponseDTO.setCount(tagsRepository.countByTagsIdOrganizationUuidEquals(getRequestTagsDTO.getOrganizationUuid()));
                }
                List<TagsResponseDTO> tagsResponseDTOS = new ArrayList<>();
                for (TagsEntity tags : tagsEntities) {
                    tagsResponseDTOS.add(tagsResponseDTO(tags));
                }
                serviceGetResponseDTO.setData(tagsResponseDTOS);
                serviceGetResponseDTO.setDescription("Get Tags Success");
                serviceGetResponseDTO.setMessage(SUCCESS);
                serviceGetResponseDTO.setCode(STATUS_2000);
            }
        } catch (Exception exception) {
            log.info("LOG :: TagsServiceImpl getTags() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceGetResponseDTO.setDescription("TagsServiceImpl getTags() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }
}
