package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.request.InitialPageRequestDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.xenia.RuleRequestRootEntity;
import com.cloudofgoods.xenia.models.RuleRequestRootObject;
import com.cloudofgoods.xenia.repository.RootRuleRepository;
import com.cloudofgoods.xenia.service.InitialPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.cloudofgoods.xenia.util.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialPageServiceImpl implements InitialPageService {

    private final RootRuleRepository rootRuleRepository;

    @Override
    public ServiceResponseDTO getCampaignForInitialPage(InitialPageRequestDTO dto) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        log.info ("LOG:: InitialPageServiceImpl getCampaignForInitialPage()");
        Instant now = Instant.now (); // 2023-10-15T11:22:11.000Z
        log.info (now.toString () + " getCampaignForInitialPage ");
        try {
            RuleRequestRootObject ruleRequestRootObject = new RuleRequestRootObject ();
            List<RuleRequestRootEntity> stream = Optional.of(dto)
                    .map(d -> d.isPagination()
                            ? rootRuleRepository.findAllByEndDateTimeIsGreaterThanEqualAndOrganizationIdEquals(Date.from(now), d.getOrganizationUuid(), PageRequest.of(d.getPage(), d.getSize()))
                            : rootRuleRepository.findAllByOrganizationIdEqualsAndEndDateTimeIsGreaterThanEqual(d.getOrganizationUuid(), Date.from(now)))
                    .orElse(null);
            long count = rootRuleRepository.countAllByOrganizationIdEqualsAndEndDateTimeIsGreaterThanEqual ( dto.getOrganizationUuid(),
                    Date.from(now));
            ruleRequestRootObject.setRuleRequestRootEntities (stream);
            ruleRequestRootObject.setTotal (count);
            serviceResponseDTO.setData (ruleRequestRootObject);
            serviceResponseDTO.setDescription ("InitialPageServiceImpl getCampaignForInitialPage Success");
            serviceResponseDTO.setMessage (SUCCESS);
            serviceResponseDTO.setCode (STATUS_2000);

        } catch (Exception exception) {
            log.info ("LOG :: InitialPageServiceImpl getCampaignForInitialPage() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setDescription ("InitialPageServiceImpl getCampaignForInitialPage() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage (FAIL);
            serviceResponseDTO.setCode (STATUS_5000);

        }
        serviceResponseDTO.setHttpStatus (STATUS_OK);
        return serviceResponseDTO;
    }
}
