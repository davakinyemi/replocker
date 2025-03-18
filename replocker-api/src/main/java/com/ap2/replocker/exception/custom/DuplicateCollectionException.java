package com.ap2.replocker.exception.custom;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class DuplicateCollectionException extends RuntimeException {
    public DuplicateCollectionException() {}
    public DuplicateCollectionException(String message) { super(message); }
}
