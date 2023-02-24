package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.TagsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.TagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TagsServiceImpl implements TagsService {
    @Override
    public ServiceResponseDTO saveOrUpdateTags(TagsDTO tagsDTO) {
        return null;
    }
}
