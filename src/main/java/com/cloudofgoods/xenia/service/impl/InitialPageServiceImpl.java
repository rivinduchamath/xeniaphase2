package com.cloudofgoods.xenia.service.impl;

import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.models.RuleRequestRootObject;
import com.cloudofgoods.xenia.repository.RootRuleRepository;
import com.cloudofgoods.xenia.service.InitialPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialPageServiceImpl implements InitialPageService {

    private final RootRuleRepository rootRuleRepository;

    @Override
    public ServiceResponseDTO getCampaignForInitialPage(String slotId, int page, int size) {
        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
        log.info ("LOG:: InitialPageServiceImpl getCampaignForInitialPage()");
        Instant now = Instant.now (); // 2023-10-15T11:22:11.000Z
        log.info (now.toString () + " getCampaignForInitialPage ");
        try {
            RuleRequestRootObject ruleRequestRootObject = new RuleRequestRootObject ();
//            List<RuleRequestRootEntity> stream = rootRuleRepository.findAllByEndDateTimeIsGreaterThanEqualAndSlotIdEquals (Date.from (now), slotId, PageRequest.of (page, size));
//            long count = rootRuleRepository.countAllByEndDateTimeIsGreaterThanEqual (Date.from (now));
//            ruleRequestRootObject.setRuleRequestRootEntities (stream);
//            ruleRequestRootObject.setTotal (count);
            serviceResponseDTO.setData (ruleRequestRootObject);
            serviceResponseDTO.setMessage ("InitialPageServiceImpl getCampaignForInitialPage Success");
            serviceResponseDTO.setMessage ("Success");
            serviceResponseDTO.setCode ("2000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        } catch (Exception exception) {
            log.info ("LOG :: InitialPageServiceImpl getCampaignForInitialPage() exception: " + exception.getMessage ());
            serviceResponseDTO.setError (exception.getStackTrace ());
            serviceResponseDTO.setMessage ("InitialPageServiceImpl getCampaignForInitialPage() exception " + exception.getMessage ());
            serviceResponseDTO.setMessage ("Fail");
            serviceResponseDTO.setCode ("5000");
            serviceResponseDTO.setHttpStatus ("OK");
            return serviceResponseDTO;
        }
    }
}
