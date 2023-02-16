package com.cloudofgoods.xenia.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandle  {

    /**
     * @param ex : java.lang.Exception
     * @return : org.springframework.http.ResponseEntity<String>
     * System Warning Exception Handler
     */
    @ExceptionHandler(value = SystemWarningException.class)
    protected ResponseEntity<String> handleRuleWarningException(Exception ex) {
        log.warn("LOG:: RuleServiceImpl saveOrUpdate() doOnError (Handle By ExceptionHandle may be Duplicate Name Or Mongo Exception)" );
        return ResponseEntity.status(598).body(ex.getMessage());
    }

}
