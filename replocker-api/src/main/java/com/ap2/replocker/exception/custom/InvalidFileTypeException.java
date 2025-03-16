package com.ap2.replocker.exception.custom;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException() {}

    public InvalidFileTypeException(String message) { super(message); }
}
