package com.cloudofgoods.xenia.exception;

/**
 * Generified Exception class to handle all the Root Level System Exceptions
 */
public class SystemRootException extends RuntimeException {

    /**
     * No Args Constructor
     */
    public SystemRootException() {
    }

    /**
     * @param message : java.lang.String
     * Single Args Contractor which accepts a String exception message as the parameter
     */
    public SystemRootException(String message) {
        super(message);
    }

    /**
     * @param message : java.lang.String
     * @param cause: java.lang.Throwable
     * Two Args Contractor which accepts a String exception message and a Throwable cause as the parameters
     */
    public SystemRootException(String message, Throwable cause) {
        super(message, cause);
    }
}
