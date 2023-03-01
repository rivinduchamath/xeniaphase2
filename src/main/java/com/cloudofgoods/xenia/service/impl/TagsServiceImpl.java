package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.config.customAnnotations.validator.NotEmptyOrNullValidator;
import com.cloudofgoods.xenia.dto.TagsDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestTagsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.dto.response.TagsResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.TagsEntity;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.repository.TagsRepository;
import com.cloudofgoods.xenia.service.TagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cloudofgoods.xenia.util.Utils.*;

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
            organizationRepository.findByUuidEquals(tagsRequestDTO.getOrganizationUuid()).ifPresentOrElse(
                    organizationEntity -> {
                        TagsEntity tags = tagsRepository.save(new TagsEntity(tagsRequestDTO.getTagsName().toUpperCase(), tagsRequestDTO.getOrganizationUuid(), tagsRequestDTO.isStatus()));
                        TagsDTO tagsResponseDTO = new TagsDTO();
                        tagsResponseDTO.setTagsName(tags.getTagsId().getTagsName());
                        tagsResponseDTO.setOrganizationUuid(tags.getTagsId().getOrganizationUuid());
                        tagsResponseDTO.setStatus(tags.isStatus());
                        serviceResponseDTO.setData(tagsResponseDTO);
                        serviceResponseDTO.setDescription("Save/Update Tags Success");
                    }, () ->
                            serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND));
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: TagsServiceImpl saveOrUpdateTags() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
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
        tagsResponseDTO.setStatus(save.isStatus());
        return tagsResponseDTO;
    }

    @Override
    public ServiceGetResponseDTO getTags(GetRequestTagsDTO getRequestTagsDTO) {
        log.info("LOG:: TagsServiceImpl getTags Service Layer");
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            List<TagsEntity> tagsEntities = Optional.of(getRequestTagsDTO)
                    .map(dto -> dto.isPagination()
                            ? tagsRepository.findAllByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(dto.getOrganizationUuid(), dto.getTagsName().toUpperCase(), PageRequest.of(dto.getPage(), dto.getSize()))
                            : tagsRepository.findAllByTagsId_OrganizationUuidEqualsAndTagsIdTagsNameStartsWith(dto.getOrganizationUuid(), dto.getTagsName().toUpperCase()))
                    .orElse(null);
            serviceGetResponseDTO.setCount(tagsRepository.countByTagsIdOrganizationUuidEqualsAndTagsIdTagsNameStartsWith(getRequestTagsDTO.getOrganizationUuid(), getRequestTagsDTO.getTagsName().toUpperCase()));
            List<TagsResponseDTO> tagsResponseDTOS = tagsEntities.stream()
                    .map(this::tagsResponseDTO)
                    .collect(Collectors.toList());
            if (!tagsResponseDTOS.isEmpty()) {
                serviceGetResponseDTO.setData(tagsResponseDTOS);
            }
            serviceGetResponseDTO.setDescription("Get Tags Success");
            serviceGetResponseDTO.setMessage(SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);

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

    @Override
    public ServiceResponseDTO activeInactiveTags(String tagsName, String organizationUuid, boolean status) {
        log.info("LOG:: TagsServiceImpl getTags Service Layer");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            organizationRepository.findOrganizationEntityByUuidEquals(organizationUuid)
                    .ifPresent(organizationEntity -> {
                        tagsRepository.findAllByTagsId_OrganizationUuidEqualsAndTagsIdTagsNameEquals(organizationUuid, tagsName.toUpperCase())
                                .ifPresentOrElse(tags -> {
                                    tags.setStatus(status);
                                    serviceResponseDTO.setData(tagsRepository.save(tags));
                                    serviceResponseDTO.setDescription("TagsServiceImpl activeInactiveTags() Success");
                                }, () -> {
                                    serviceResponseDTO.setDescription("TagsServiceImpl activeInactiveTags() attribute Not Found");
                                });
                    });
            if (!NotEmptyOrNullValidator.isNotNullOrEmpty(serviceResponseDTO.getDescription())) {
                serviceResponseDTO.setDescription("TagsServiceImpl activeInactiveTags() Organization Not Found");
            }
            serviceResponseDTO.setMessage(SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: TagsServiceImpl activeInactiveTags() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            exception.printStackTrace();
            serviceResponseDTO.setDescription("TagsServiceImpl activeInactiveTags() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }


    @Override
    public ServiceGetResponseDTO getSingleTags(TagsDTO tagsDTO) {
        log.info("LOG:: TagsServiceImpl getSingleTags Service Layer");
        ServiceGetResponseDTO serviceGetResponseDTO = new ServiceGetResponseDTO();
        try {
            tagsRepository.findAllByTagsId_OrganizationUuidEqualsAndTagsIdTagsNameEquals(tagsDTO.getOrganizationUuid(), tagsDTO.getTagsName().toUpperCase())
                    .ifPresent(tags -> {
                        serviceGetResponseDTO.setData(tags);
                        serviceGetResponseDTO.setDescription("Get Tags Success");
                    });
            if (!NotEmptyOrNullValidator.isNotNullOrEmpty(serviceGetResponseDTO.getDescription())) {
                serviceGetResponseDTO.setDescription("Cannot Find Data");
            }
            serviceGetResponseDTO.setMessage(SUCCESS);
            serviceGetResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: TagsServiceImpl getSingleTags() exception: " + exception.getMessage());
            serviceGetResponseDTO.setError(exception.getStackTrace());
            serviceGetResponseDTO.setDescription("TagsServiceImpl getSingleTags() exception " + exception.getMessage());
            serviceGetResponseDTO.setMessage(FAIL);
            serviceGetResponseDTO.setCode(STATUS_5000);
        }
        serviceGetResponseDTO.setHttpStatus(STATUS_OK);
        return serviceGetResponseDTO;
    }

}
