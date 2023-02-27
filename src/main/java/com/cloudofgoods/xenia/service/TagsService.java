package com.cloudofgoods.xenia.service;

import com.cloudofgoods.xenia.dto.TagsDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestTagsDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;

public interface TagsService {
    ServiceResponseDTO saveOrUpdateTags(TagsDTO tagsDTO);

    ServiceGetResponseDTO getTags(GetRequestTagsDTO getRequestTagsDTO);
}
