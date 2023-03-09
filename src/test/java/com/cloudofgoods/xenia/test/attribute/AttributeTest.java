package com.cloudofgoods.xenia.test.attribute;

import com.cloudofgoods.xenia.dto.request.AttributeRequestDTO;
import com.cloudofgoods.xenia.dto.request.GetRequestAttributeDTO;
import com.cloudofgoods.xenia.dto.response.AttributeResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceGetResponseDTO;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.service.AttributesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static com.cloudofgoods.xenia.util.Utils.STATUS_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@Slf4j
public class AttributeTest {
    @Autowired
    private AttributesService attributesService;
    private final String organizationUuid = "COG";
    private final String attribute = "attribute";


    @Test
    void saveAttributeAndAfterUpdate(){
        AttributeRequestDTO attributeRequestDTO = new AttributeRequestDTO();
        attributeRequestDTO.setAttributeName(attribute + "Test");
        attributeRequestDTO.setOrganizationUuid(organizationUuid);
        attributeRequestDTO.setDisplayName("AttributeTest");
        attributeRequestDTO.setTableName("tableName");
        attributeRequestDTO.setStatus(true);
        attributeRequestDTO.setType("Enum");
        log.info("-------------------------------------------------------------------");
        log.info("\nSave Attribute DTO : "+ attributeRequestDTO +" \n");
        ServiceResponseDTO serviceResponseDTO = attributesService.saveAttribute(attributeRequestDTO);
        log.info("\nSave Attribute DTO Response : "+ serviceResponseDTO +" \n");
        AttributeResponseDTO attributeResponseDTO = (AttributeResponseDTO) serviceResponseDTO.getData();
        attributeRequestDTO.setAttributeUuid( attributeResponseDTO.getAttributeUuid());
        log.info("\nAfter Save Attribute Again Update Attribute Using AttributeUuid : "+ attributeResponseDTO.getAttributeUuid() +" \n");
        List<Object> strings = new ArrayList<>();
        strings.add("buddhist" );
        attributeRequestDTO.setValues(strings);
        log.info("\nAttribute DTO Update : "+ attributeRequestDTO +" \n");
        ServiceResponseDTO serviceResponseDTO1 = attributesService.saveAttribute(attributeRequestDTO);
        log.info("\nUpdate Attribute DTO Response : "+ serviceResponseDTO +" \n");
        log.info("-------------------------------------------------------------------");
        assertEquals(STATUS_SUCCESS,serviceResponseDTO.getMessage());
        assertEquals(STATUS_SUCCESS,serviceResponseDTO1.getMessage());
    }
    @Test
    void saveAttributeWithValuesAndAfterActiveAndInactive(){
        AttributeRequestDTO attributeRequestDTO = new AttributeRequestDTO();
        attributeRequestDTO.setAttributeName( attribute +"Test2");
        attributeRequestDTO.setOrganizationUuid(organizationUuid);
        attributeRequestDTO.setDisplayName("AttributeTest2");
        attributeRequestDTO.setTableName("tableName2");
        attributeRequestDTO.setStatus(false);
        attributeRequestDTO.setType("Enum");
        List<Object> strings = new ArrayList<>();
        strings.add("buddhist2" );
        attributeRequestDTO.setValues(strings);
        log.info("-------------------------------------------------------------------");
        log.info("\nSave Attribute DTO With All : "+ attributeRequestDTO +" \n");
        log.info("-------------------------------------------------------------------");
        ServiceResponseDTO serviceResponseDTO = attributesService.saveAttribute(attributeRequestDTO);
        AttributeResponseDTO attributeResponseDTO = (AttributeResponseDTO) serviceResponseDTO.getData();
        log.info("-------------------------------------------------------------------");
        activeInactiveAttribute(attributeResponseDTO.getAttributeUuid() , true);
        activeInactiveAttribute(attributeResponseDTO.getAttributeUuid() , false);
        log.info("-------------------------------------------------------------------");
        assertEquals(STATUS_SUCCESS,serviceResponseDTO.getMessage());
    }


    void  activeInactiveAttribute(String attributeUuid, boolean status){
        ServiceResponseDTO serviceResponseDTO = attributesService.activeInactiveAttribute(attributeUuid, organizationUuid, status);
        log.info("\nActive Inactive Attribute : "+ serviceResponseDTO +" \n");
        assertEquals(STATUS_SUCCESS,serviceResponseDTO.getMessage());
    }

    @Test
    void getAttributes(){
        log.info("-------------------------------------------------------------------");
        GetRequestAttributeDTO getRequestAttributeDTO = new GetRequestAttributeDTO();
        getRequestAttributeDTO.setAttributeName(attribute);
        getRequestAttributeDTO.setOrganizationUuid(organizationUuid);
        getRequestAttributeDTO.setPage(0);
        getRequestAttributeDTO.setSize(10);
        getRequestAttributeDTO.setType(Collections.singletonList("Enum"));
        getRequestAttributeDTO.setPagination(false);
        ServiceGetResponseDTO attribute = attributesService.getAttribute(getRequestAttributeDTO);
        log.info("\nGet Attribute : " + attribute.getData() +" \n");
        log.info("-------------------------------------------------------------------");
        assertEquals(STATUS_SUCCESS , attribute.getMessage());
    }
}
