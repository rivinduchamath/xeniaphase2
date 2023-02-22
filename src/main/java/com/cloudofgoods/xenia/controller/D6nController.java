package com.cloudofgoods.xenia.controller;

import com.cloudofgoods.xenia.dto.D6nDTO;
import com.cloudofgoods.xenia.dto.caution.User;
import com.cloudofgoods.xenia.dto.response.ServiceResponseDTO;
import com.cloudofgoods.xenia.entity.AuthUser;
import com.cloudofgoods.xenia.repository.UserRepository;
import com.cloudofgoods.xenia.service.D6nService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/d6n")
@RequiredArgsConstructor
public class D6nController {
    private final D6nService d6nService;
    private final UserRepository users;

    @Description("Create This API For makeDecision (Personalise)")
    @PostMapping(value = "${server.servlet.generateVariant}")
    public ServiceResponseDTO generateVariant(@RequestBody D6nDTO d6nDTO) {
        log.info ("LOG:: D6nController makeDecision calling " + " user " + d6nDTO.getUserEmail() + "OrganizationEntity" + d6nDTO.getOrganization() + " channels " + d6nDTO.getChannels().toString () + " slot " + d6nDTO.getSlot().toString ());
        AuthUser user = null;
        User userDTO = new User ();

        if(d6nDTO.getUserEmail() != null) {
             user = this.users.findByUsername(d6nDTO.getUserEmail());
            LinkedHashMap <String, Object> linkedHashMap = new LinkedHashMap <String, Object> ();
            linkedHashMap.put ("age", user.getAge ());
            linkedHashMap.put ("hobby", user.getHobby ());
            linkedHashMap.put ("roles", user.getRoles ());
            linkedHashMap.put ("country", user.getCountry ());
            linkedHashMap.put ("religion", user.getReligion ());
            linkedHashMap.put ("maritalStatus", user.getMaritalStatus ());
            userDTO.setUserData (linkedHashMap);
        }else {

        }
        return d6nService.makeDecision (d6nDTO.getNumberOfResponseFrom() ,d6nDTO.getNumberOfResponse(), d6nDTO.getUserEmail(), userDTO, d6nDTO.getChannels().stream()
                .map(String::toUpperCase).collect(Collectors.toList()), d6nDTO.getSlot(), d6nDTO.getOrganization().toUpperCase());

    }

//    @Description("Create This API For getAttributes (Personalise)")
//    @GetMapping(value = "${server.servlet.attribute}")
//    public ServiceResponseDTO getAttributes() {
//        ServiceResponseDTO serviceResponseDTO = new ServiceResponseDTO ();
//        try {
//            List <ResponseAttribute> attributes = responseAttributes ();
//            serviceResponseDTO.setHttpStatus ("OK");
//            serviceResponseDTO.setData (attributes);
//            serviceResponseDTO.setDescription ("getAttributes Success");
//            serviceResponseDTO.setCode ("2000");
//            return serviceResponseDTO;
//        } catch (Exception exception) {
//            log.info ("LOG:: D6nController makeDecision Exception :: " + exception.getMessage ());
//            serviceResponseDTO.setError (exception.getStackTrace ());
//            serviceResponseDTO.setMessage ("ChannelServiceImpl getChannels() exception " + exception.getMessage ());
//            serviceResponseDTO.setMessage ("Fail");
//            serviceResponseDTO.setCode ("5000");
//            serviceResponseDTO.setHttpStatus ("OK");
//            return serviceResponseDTO;
//        }
//    }

//    List <Object> responseOperators(String type) {
//        List <Object> val = new ArrayList <> ();
//        switch (type) {
//            case "list": {
//                List <ResponseAttributeDTO> responseAttributeModel3 = new ArrayList <> ();
//                responseAttributeModel3.add (new ResponseAttributeDTO ("contains", ""));
//                responseAttributeModel3.add (new ResponseAttributeDTO ("notContains", ""));
//                val.add (responseAttributeModel3);
//            }
//            case "double": {
//                List <ResponseAttributeDTO> ResponseAttributeDTO = new ArrayList <> ();
//                ResponseAttributeDTO.add (new ResponseAttributeDTO ("equal", "=="));
//                ResponseAttributeDTO.add (new ResponseAttributeDTO ("NotEqual", "!="));
//                ResponseAttributeDTO.add (new ResponseAttributeDTO ("graterThan", "<"));
//                ResponseAttributeDTO.add (new ResponseAttributeDTO ("lesserThan", ">"));
//                ResponseAttributeDTO.add (new ResponseAttributeDTO ("graterThanOrEqual", "<="));
//                ResponseAttributeDTO.add (new ResponseAttributeDTO ("lesserThanOrEqual", ">="));
//                val.add (ResponseAttributeDTO);
//            }
//            case "string": {
//                List <ResponseAttributeDTO> responseAttributeModel2 = new ArrayList <> ();
//                responseAttributeModel2.add (new ResponseAttributeDTO ("equal", ""));
//                responseAttributeModel2.add (new ResponseAttributeDTO ("NotEqual", ""));
//                val.add (responseAttributeModel2);
//            }
//        }
//        return val;
//    }

//    List <ResponseAttribute> responseAttributes() {
//        List <ResponseAttribute> attributes = new ArrayList <ResponseAttribute> ();
//
//        ResponseAttribute responseAttribute = new ResponseAttribute ();
//        responseAttribute.setLabel ("Age");
//        responseAttribute.setValue ("age");
//        responseAttribute.setAttrType ("double");
//        responseAttribute.setType ("double");
//        responseAttribute.setOperators (responseOperators ("double"));
//        attributes.add (responseAttribute);
//
//        ResponseAttribute responseAttribute2 = new ResponseAttribute ();
//        responseAttribute2.setLabel ("Country");
//        responseAttribute2.setValue ("country");
//        responseAttribute2.setAttrType ("string");
//        responseAttribute2.setType ("enum");
//        List <Country> countries = new ArrayList <Country> ();
//        countries.add (Country.UK);
//        countries.add (Country.USA);
//        countries.add (Country.Mexico);
//        responseAttribute2.setValues (Arrays.asList (countries.toArray ()));
//        responseAttribute2.setOperators (responseOperators ("string"));
//        attributes.add (responseAttribute2);
//
//        ResponseAttribute responseAttribute4 = new ResponseAttribute ();
//        responseAttribute4.setLabel ("Religion");
//        responseAttribute4.setValue ("religion");
//        responseAttribute4.setAttrType ("string");
//        responseAttribute4.setType ("enum");
//        List <Religion> religions = new ArrayList <Religion> ();
//        religions.add (Religion.Buddhist);
//        religions.add (Religion.Hindu);
//        religions.add (Religion.Christians);
//        religions.add (Religion.Muslim);
//        responseAttribute4.setOperators (responseOperators ("string"));
//        responseAttribute4.setValues (Arrays.asList (religions.toArray ()));
//        attributes.add (responseAttribute4);
//
//        ResponseAttribute string = new ResponseAttribute ();
//        string.setLabel ("Hobby");
//        string.setValue ("hobby");
//        string.setAttrType ("string");
//        string.setType ("string");
//        string.setOperators (responseOperators ("string"));
//        attributes.add (string);
//
//        ResponseAttribute maritalres = new ResponseAttribute ();
//        maritalres.setLabel ("Marital");
//        maritalres.setValue ("maritalStatus");
//        maritalres.setAttrType ("string");
//        maritalres.setType ("enum");
//        List <Marital> marital = new ArrayList <Marital> ();
//        marital.add (Marital.Married);
//        marital.add (Marital.UnMarried);
//        maritalres.setOperators (responseOperators ("string"));
//        maritalres.setValues (Arrays.asList (marital.toArray ()));
//        attributes.add (maritalres);
//        return attributes;
//    }
}
