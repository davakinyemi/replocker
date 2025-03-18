package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class DuplicateDomainException extends RuntimeException {
    public DuplicateDomainException() {}
    public DuplicateDomainException(String message) { super(message); }
}
