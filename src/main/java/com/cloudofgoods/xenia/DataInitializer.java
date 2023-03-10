package com.cloudofgoods.xenia;

import com.cloudofgoods.xenia.controller.controllenums.Country;
import com.cloudofgoods.xenia.controller.controllenums.Marital;
import com.cloudofgoods.xenia.controller.controllenums.Religion;
import com.cloudofgoods.xenia.entity.AuthUser;
import com.cloudofgoods.xenia.entity.xenia.OrganizationEntity;
import com.cloudofgoods.xenia.repository.OrganizationRepository;
import com.cloudofgoods.xenia.repository.UserRepository;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository users;

    @Autowired
    private OrganizationRepository organizationRepository;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        users.deleteAll();

        List<String> strings = new ArrayList<>();
        AuthUser user = AuthUser.builder()
                .roles(strings)
                .age(22)
                .country(String.valueOf(Country.USA))
                .religion(String.valueOf(Religion.Buddhist))
                .maritalStatus(String.valueOf(Marital.Married))
                .username("adminuser")
                .roles(List.of(new String[]{"ADMIN", "USER"}))
                .hobby("Movie")
                .password(("password"))
                .email("adminuser@gmail.com")
                .build();
        this.users.save(user);

        AuthUser user2 = AuthUser.builder()
                .roles(strings)
                .age(55)
                .country(String.valueOf(Country.USA))
                .religion(String.valueOf(Religion.Christians))
                .maritalStatus(String.valueOf(Marital.Married))
                .username("user")
                .roles(List.of(new String[]{"USER"}))
                .hobby("Cricket")
                .password(("password"))
                .email("user@gmail.com")
                .build();
        this.users.save(user2);

        AuthUser user3 = AuthUser.builder()
                .roles(strings)
                .age(44)
                .country(String.valueOf(Country.Mexico))
                .religion(String.valueOf(Religion.Hindu))
                .maritalStatus(String.valueOf(Marital.UnMarried))
                .username("abc")
                .roles(List.of(new String[]{"ADMIN", "USER"}))
                .hobby("Cricket")
                .password(("password"))
                .email("abc@gmail.com")
                .build();
        this.users.save(user3);

        AuthUser user4 = AuthUser.builder()
                .roles(strings)
                .age(66)
                .country(String.valueOf(Country.USA))
                .religion(String.valueOf(Religion.Christians))
                .maritalStatus(String.valueOf(Marital.UnMarried))
                .username("kamal")
                .roles(List.of(new String[]{"USER"}))
                .hobby("Carom")
                .password(("password"))
                .email("kamal@gmail.com")
                .build();
        this.users.save(user4);

        AuthUser user5 = AuthUser.builder()
                .roles(strings)
                .age(33)
                .country(String.valueOf(Country.Mexico))
                .religion(String.valueOf(Religion.Buddhist))
                .maritalStatus(String.valueOf(Marital.Married))
                .username("Nimal")
                .roles(List.of(new String[]{"USER"}))
                .hobby("Movie")
                .password(("password"))
                .email("nimal@gmail.com")
                .build();
        this.users.save(user5);

        long count = organizationRepository.count();
        if (count == 0) {
            OrganizationEntity organizationEntity = new OrganizationEntity();
            log.info("LOG:: CampaignTemplateServiceImpl saveTemplate Save");
            organizationEntity.setName("COG");
            NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
            UUID firstUUID = timeBasedGenerator.generate();
            organizationEntity.setUuid("COG" + "");
            organizationEntity.setPassword("1234");
            organizationRepository.save(organizationEntity); // Save
        }
    }
}
