package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.models.TemplateCustomObject;
import com.cloudofgoods.xenia.entity.xenia.SegmentTemplateEntity;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.repository.SegmentTemplateRepository;
import com.cloudofgoods.xenia.service.TemplateService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cloudofgoods.xenia.util.Utils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SegmentTemplateServiceImpl implements TemplateService {
    private final OrganizationRepository organizationRepository;

    private final SegmentTemplateRepository segmentTemplateRepository;


    @Override
    public ServiceResponseDTO getAllTemplate() {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            List<SegmentTemplateEntity> all = segmentTemplateRepository.findAll();
            serviceResponseDTO.setData(all);
            serviceResponseDTO.setDescription("TemplateServiceImpl getAllTemplate Success");
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getAllTemplate() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getAllTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getTemplateByName(String name) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<SegmentTemplateEntity> template = segmentTemplateRepository.findAllBySegmentNameStartsWith(name);
            serviceResponseDTO.setData(template);
            if (template.isPresent()) {
                serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateByName Success");
            } else {
                serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateByName Success :: NO DATA");
            }
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getTemplateByName() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateByName() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO getTemplateById(String templateId) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<SegmentTemplateEntity> template = segmentTemplateRepository.findById(templateId);
            serviceResponseDTO.setData(template);
            if (template.isPresent()) {
                serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateById Success");
                serviceResponseDTO.setMessage(STATUS_SUCCESS);
                serviceResponseDTO.setCode(STATUS_2000);
            } else {
                serviceResponseDTO.setMessage("TemplateServiceImpl getTemplateById Success :: NO DATA");
            }
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getTemplateById() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateById() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }

    @Override
    public ServiceResponseDTO deleteTemplateById(String templateId) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            segmentTemplateRepository.deleteById(templateId);
            serviceResponseDTO.setDescription("TemplateServiceImpl deleteTemplateById Success");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl deleteTemplateById() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl deleteTemplateById() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
            serviceResponseDTO.setHttpStatus(STATUS_OK);
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO saveTemplate(SegmentTemplateEntity ruleRootModel) {
        log.info("LOG:: TemplateServiceImpl saveTemplate");
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        organizationRepository.findByUuidEquals(ruleRootModel.getOrganizationUuid()).ifPresentOrElse(
                organizationEntity -> {
                    try {
                        if (ruleRootModel.getId() != null) {
                            SegmentTemplateEntity save = segmentTemplateRepository.save(ruleRootModel);
                            serviceResponseDTO.setDescription("Update Template Success");
                            serviceResponseDTO.setData(save);
                        } else {
                            ruleRootModel.setSegmentName(saveTemplateNameGenerator(ruleRootModel.getSegmentName()));
                            SegmentTemplateEntity save = segmentTemplateRepository.save(ruleRootModel);
                            serviceResponseDTO.setDescription("Save Template Success");
                            serviceResponseDTO.setData(save);
                        }
                        serviceResponseDTO.setMessage(STATUS_SUCCESS);
                        serviceResponseDTO.setCode(STATUS_2000);

                    } catch (Exception exception) {
                        log.info("LOG :: TemplateServiceImpl saveTemplate() exception: " + exception.getMessage());
                        serviceResponseDTO.setError(exception.getStackTrace());
                        serviceResponseDTO.setDescription("TemplateServiceImpl saveTemplate() exception " + exception.getMessage());
                        serviceResponseDTO.setMessage(STATUS_FAIL);
                        serviceResponseDTO.setCode(STATUS_5000);
                    }
                }, ()->   serviceResponseDTO.setDescription(ORGANIZATION_NOT_FOUND)
        );
        return serviceResponseDTO;
    }

    private String saveTemplateNameGenerator(String templateName) {
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        UUID firstUUID = timeBasedGenerator.generate();
        SegmentTemplateEntity segmentTemplateEntity = new SegmentTemplateEntity();
        templateName = templateName + "##$$$##" + firstUUID.timestamp();
        segmentTemplateEntity.setSegmentName(templateName);
        return templateName;
    }

    @Override
    public ServiceResponseDTO getAllTemplatePagination(int page, int size) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            log.info("LOG:: TemplateServiceImpl getAllTemplatePagination()");
            List<SegmentTemplateEntity> templateEntities = segmentTemplateRepository.findAllBySegmentNameNotNull(PageRequest.of(page, size));
            long count = segmentTemplateRepository.count();
            TemplateCustomObject templateCustomDTO = new TemplateCustomObject();
            templateCustomDTO.setTemplateEntities(templateEntities);
            templateCustomDTO.setTotal(count);
            serviceResponseDTO.setData(templateCustomDTO);
            serviceResponseDTO.setMessage(STATUS_SUCCESS);
            serviceResponseDTO.setCode(STATUS_2000);
            serviceResponseDTO.setDescription("Get Template Success");
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getAllTemplatePagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getAllTemplatePagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage(STATUS_FAIL);
            serviceResponseDTO.setCode(STATUS_5000);
        }
        serviceResponseDTO.setHttpStatus(STATUS_OK);
        return serviceResponseDTO;
    }
}
