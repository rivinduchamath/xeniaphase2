package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.TagsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface TagsService {
    ServiceResponseDTO saveOrUpdateTags(TagsDTO tagsDTO);
}
