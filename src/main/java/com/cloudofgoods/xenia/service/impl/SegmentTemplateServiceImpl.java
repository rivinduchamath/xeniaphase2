package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.models.TemplateCustomObject;
import com.cloudofgoods.xenia.entity.xenia.SegmentTemplateEntity;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.repository.SegmentTemplateRepository;
import com.cloudofgoods.xenia.service.TemplateService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getAllTemplate() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getAllTemplate() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");

            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO getTemplateByName(String name) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<SegmentTemplateEntity> template = segmentTemplateRepository.findAllBySegmentNameStartsWith(name);
            serviceResponseDTO.setData(template);
            if (template.isPresent()) {
                serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateByName Success");
                serviceResponseDTO.setMessage("Success");
                serviceResponseDTO.setCode("2000");
                serviceResponseDTO.setHttpStatus("OK");

            } else {
                serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateByName Success :: NO DATA");
                serviceResponseDTO.setMessage("Success");
                serviceResponseDTO.setCode("2000");
                serviceResponseDTO.setHttpStatus("OK");

            }
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getTemplateByName() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateByName() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO getTemplateById(String templateId) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        try {
            Optional<SegmentTemplateEntity> template = segmentTemplateRepository.findById(templateId);
            serviceResponseDTO.setData(template);
            if (template.isPresent()) {
                serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateById Success");
                serviceResponseDTO.setMessage("Success");
                serviceResponseDTO.setCode("2000");
                serviceResponseDTO.setHttpStatus("OK");
            } else {
                serviceResponseDTO.setMessage("TemplateServiceImpl getTemplateById Success :: NO DATA");
            }
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getTemplateById() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getTemplateById() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
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
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }

    @Override
    public ServiceResponseDTO saveTemplate(SegmentTemplateEntity ruleRootModel) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO();
        log.info("LOG:: TemplateServiceImpl saveTemplate");
        Optional<OrganizationEntity> byUuid = organizationRepository.findByUuidEquals(ruleRootModel.getOrganizationUuid());
        if (byUuid.isPresent()) {
                try {
                    if (ruleRootModel.getId() != null) {
                        SegmentTemplateEntity save = segmentTemplateRepository.save(ruleRootModel);
                        serviceResponseDTO.setDescription("Update Template Success");
                        serviceResponseDTO.setMessage("Success");
                        serviceResponseDTO.setCode("2000");
                        serviceResponseDTO.setHttpStatus("OK");
                        serviceResponseDTO.setData(save);
                    } else {
                        ruleRootModel.setSegmentName(saveTemplateNameGenerator(ruleRootModel.getSegmentName()));
                        SegmentTemplateEntity save = segmentTemplateRepository.save(ruleRootModel);
                        serviceResponseDTO.setDescription("Save Template Success");
                        serviceResponseDTO.setMessage("Success");
                        serviceResponseDTO.setCode("2000");
                        serviceResponseDTO.setHttpStatus("OK");
                        serviceResponseDTO.setData(save);
                    }
                    return serviceResponseDTO;
                } catch (Exception exception) {
                    log.info("LOG :: TemplateServiceImpl saveTemplate() exception: " + exception.getMessage());
                    serviceResponseDTO.setError(exception.getStackTrace());
                    serviceResponseDTO.setDescription("TemplateServiceImpl saveTemplate() exception " + exception.getMessage());
                    serviceResponseDTO.setMessage("Fail");
                    serviceResponseDTO.setCode("5000");
                    serviceResponseDTO.setHttpStatus("OK");
                    return serviceResponseDTO;
                }
        }
        serviceResponseDTO.setDescription("Organization Uuid Not Found");
        serviceResponseDTO.setMessage("Success");
        serviceResponseDTO.setCode("2000");
        serviceResponseDTO.setHttpStatus("OK");
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
            serviceResponseDTO.setMessage("Success");
            serviceResponseDTO.setCode("2000");
            serviceResponseDTO.setHttpStatus("OK");
            serviceResponseDTO.setDescription("Get Template Success");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info("LOG :: TemplateServiceImpl getAllTemplatePagination() exception: " + exception.getMessage());
            serviceResponseDTO.setError(exception.getStackTrace());
            serviceResponseDTO.setDescription("TemplateServiceImpl getAllTemplatePagination() exception " + exception.getMessage());
            serviceResponseDTO.setMessage("Fail");
            serviceResponseDTO.setCode("5000");
            serviceResponseDTO.setHttpStatus("OK");
            return serviceResponseDTO;
        }
    }
}
