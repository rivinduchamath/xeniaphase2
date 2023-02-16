package com.cloudofgoods.xenia.controller.controllconfig;


import com.cloudofgoods.xenia.exception.SystemRootException;
import com.cloudofgoods.xenia.exception.SystemWarningException;
import com.cloudofgoods.xenia.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 * Create This class to handle Exception
 * */
@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    @ExceptionHandler(value = SystemWarningException.class)
    protected ResponseEntity<String> handleCogWarningException(Exception ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(598).body(ex.getMessage());
    }

    @ExceptionHandler(value = {SystemRootException.class, Exception.class})
    protected ResponseEntity<String> handleCogInternalServerException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Utility.EX_ROOT);
    }
}
