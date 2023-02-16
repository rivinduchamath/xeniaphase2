package com.cloudofgoods.xenia.exception;

/**
 * Generified Exception class to handle all the System Warning Level Exceptions
 */
public class SystemWarningException extends SystemRootException {

    /**
     * No Args Constructor
     */
    public SystemWarningException() {
    }

    /**
     * @param message : java.lang.String
     * Single Args Contractor which accepts a String exception message as the parameter
     */
    public SystemWarningException(String message) {
        super(message);
    }

    /**
     * @param message : java.lang.String
     * @param cause: java.lang.Throwable
     * Two Args Contractor which accepts a String exception message and a Throwable cause as the parameters
     */
    public SystemWarningException(String message, Throwable cause) {
        super(message, cause);
    }
}
