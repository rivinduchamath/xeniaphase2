package com.cloudofgoods.xenia.controller.redis;

import com.cloudofgoods.xenia.dto.D6nResponseModelDTO;
import com.cloudofgoods.xenia.dto.redis.RedisOrganizationRequestDTO;
import com.cloudofgoods.xenia.dto.redis.RedisCommonResponseDTO;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/d6n/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisTemplate <String, Object> redisTemplate;

    @PostMapping(value = "${server.servlet.getAllValues}")
    @Description("Add AttributesObject")
    public RedisCommonResponseDTO getAllValuesFromAllOrganizations() {
        log.info ("LOG:: RedisController getAllValues");
        RedisCommonResponseDTO responseDTO = new RedisCommonResponseDTO ();

        try (RedisConnection redisConnection = Objects.requireNonNull (redisTemplate.getConnectionFactory ()).getConnection ()) {
            ScanOptions options = ScanOptions.scanOptions ().match ("organization*")/*.count (100)*/.build ();
            Cursor <byte[]> c = redisConnection.scan (options);
            long successValues = 0;
            long allValues = 0;
            while (c.hasNext ()) {
                String key = new String (c.next ());
                D6nResponseModelDTO value = (D6nResponseModelDTO) redisTemplate.opsForValue ().get (key);
                assert value != null;
                if (value.getVariant () != null) successValues++;
                allValues++;
            }
            responseDTO.setAllCount (allValues + "");
            responseDTO.setSuccessCount (successValues + "");
            responseDTO.setCode ("2000");
            responseDTO.setMessage ("Success");
            responseDTO.setDescription ("Get All Values From All Organizations With Out Specific Date Range (Under organization Cache).");

        } catch (Exception exception) {
            log.info ("LOG:: RedisController getAllValues exception " + exception.getMessage ());
            responseDTO.setCode ("5000");
            responseDTO.setError (exception.getStackTrace ());
            responseDTO.setMessage ("Fail");
            responseDTO.setDescription ("Get All Values From All Organizations With Out Specific Date Range (Under organization Cache) Exception.");

        } finally {
            responseDTO.setHttpStatus ("OK");
            responseDTO.setDateRange ("Not Specific");
            responseDTO.setOrganization ("All Organizations (Under organization Cache Manager)");
        }
        return responseDTO;
    }

    @PostMapping(value = "${server.servlet.getOrganizationMaxValues}")
    @Description("Get Max Rules Under OrganizationEntity")
    public RedisCommonResponseDTO getOrganizationMaxValues(@RequestBody RedisOrganizationRequestDTO redisOrganizationRequestDTO) {
        RedisCommonResponseDTO responseDTO = new RedisCommonResponseDTO ();
        try (RedisConnection redisConnection = Objects.requireNonNull (redisTemplate.getConnectionFactory ()).getConnection ()) {
            long allValues = 0;
            ScanOptions options = ScanOptions.scanOptions ().match (
                    "organization::" + redisOrganizationRequestDTO.getOrganization ().toUpperCase () + "*").build ();
            Cursor <byte[]> c = redisConnection.scan (options);
            long success = 0;
            String key = new String (c.next ());// Get Key
            D6nResponseModelDTO value = (D6nResponseModelDTO) redisTemplate.opsForValue ().get (key);// Get Value
            List <String> valueList = new ArrayList <> ();


        }catch (Exception exception){

        }
        return responseDTO;
    }
        @PostMapping(value = "${server.servlet.getOrganizationValues}")
    @Description("Add AttributesObject")
    public RedisCommonResponseDTO getOrganizationValues(@RequestBody RedisOrganizationRequestDTO redisOrganizationRequestDTO) {
        log.info ("LOG:: RedisController getOrganizationValues");
        RedisCommonResponseDTO responseDTO = new RedisCommonResponseDTO ();
        try (RedisConnection redisConnection = Objects.requireNonNull (redisTemplate.getConnectionFactory ()).getConnection ()) {
            long allValues = 0;
            ScanOptions options = ScanOptions.scanOptions ().match (
                    "organization::" + redisOrganizationRequestDTO.getOrganization ().toUpperCase () + "*").build ();
            Cursor <byte[]> c = redisConnection.scan (options);
            long success = 0;
            while (c.hasNext ()) { // If Values are there, Go Inside
                String key = new String (c.next ());// Get Key
                D6nResponseModelDTO value = (D6nResponseModelDTO) redisTemplate.opsForValue ().get (key);// Get Value

                if (redisOrganizationRequestDTO.getStartsDate () != null && redisOrganizationRequestDTO.getEndDate () != null) { // If End Date and Start Date Both Not Null
                    LocalDateTime startDate = redisOrganizationRequestDTO.getStartsDate ();
                    LocalDateTime endDate = redisOrganizationRequestDTO.getEndDate ();
                    int startIndex = key.indexOf ("##$$##") + 6;
                    int endIndex = key.lastIndexOf ("##$$##");
                    String dateToCheck = key.substring (startIndex, endIndex);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("yyyy/MM/ddHH:mm:ss.SSSZ");
                    LocalDateTime checkDate = LocalDateTime.parse (dateToCheck, formatter);

                    if (checkDate.isAfter (startDate) && checkDate.isBefore (endDate)) {// Get All With In the Date Range
                        assert value != null;
                        if (value.getVariant () != null) success++;
                        allValues++;
                    }
                }
                if (redisOrganizationRequestDTO.getStartsDate () != null && redisOrganizationRequestDTO.getEndDate () == null) {
                    LocalDateTime startDate = redisOrganizationRequestDTO.getStartsDate ();
                    int startIndex = key.indexOf ("##$$##") + 6;
                    int endIndex = key.lastIndexOf ("##$$##");
                    String dateToCheck = key.substring (startIndex, endIndex);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("yyyy/MM/ddHH:mm:ss.SSSZ");
                    LocalDateTime checkDate = LocalDateTime.parse (dateToCheck, formatter);

                    if (checkDate.isAfter (startDate)) {
                        assert value != null;
                        if (value.getVariant () != null) success++;
                        allValues++;
                    }
                }
                if (redisOrganizationRequestDTO.getStartsDate () == null && redisOrganizationRequestDTO.getEndDate () != null) {
                    var endDate = redisOrganizationRequestDTO.getEndDate ();
                    int startIndex = key.indexOf ("##$$##") + 6;
                    int endIndex = key.lastIndexOf ("##$$##");
                    String dateToCheck = key.substring (startIndex, endIndex);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("yyyy/MM/ddHH:mm:ss.SSSZ");
                    LocalDateTime checkDate = LocalDateTime.parse (dateToCheck, formatter);

                    if (checkDate.isBefore (endDate)) {
                        assert value != null;
                        if (value.getVariant () != null) success++;
                        allValues++;
                    }
                }else {
                    assert value != null;
                    if (value.getVariant () != null) success++;
                    allValues++;
                }
            }
            responseDTO.setAllCount (allValues + "");
            responseDTO.setSuccessCount (success + "");
            responseDTO.setCode ("2000");
            responseDTO.setMessage ("Success");
            responseDTO.setDescription ("Get All Values From " + redisOrganizationRequestDTO.getOrganization ().toUpperCase () + " OrganizationEntity " +
                    "With Date Range " + redisOrganizationRequestDTO.getStartsDate () + " To " + redisOrganizationRequestDTO.getEndDate ());

        } catch (Exception exception) {
            responseDTO.setCode ("5000");
            responseDTO.setError (exception.getStackTrace ());
            responseDTO.setMessage ("Fail");
            responseDTO.setDescription ("Get All Values From " + redisOrganizationRequestDTO.getOrganization ().toUpperCase () + " OrganizationEntity " +
                    "With Date Range " + redisOrganizationRequestDTO.getStartsDate () + " To " + redisOrganizationRequestDTO.getEndDate () + " Exception");
            log.info ("LOG:: RedisController getOrganizationValues exception " + exception.getMessage ());
        } finally {
            responseDTO.setHttpStatus ("OK");
            responseDTO.setDateRange (redisOrganizationRequestDTO.getStartsDate () + " To " + redisOrganizationRequestDTO.getEndDate ());
            responseDTO.setOrganization (redisOrganizationRequestDTO.getOrganization ().toUpperCase ());
            log.info ("LOG:: RedisController getOrganizationValues redisConnection.close ()");
        }
        return responseDTO;
    }
}
