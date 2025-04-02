package com.ap2.replocker.exception.custom;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class DuplicateRequestException extends RuntimeException {
    public DuplicateRequestException(String email, String collectionName) { super(email + ": " + collectionName); }
}
