package com.ap2.replocker.exception.custom;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class DuplicateReportException extends RuntimeException {
    public DuplicateReportException(String message, String name) { super(message + ": " + name); }
}
