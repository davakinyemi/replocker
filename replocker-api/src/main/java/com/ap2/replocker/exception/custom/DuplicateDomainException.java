package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class DuplicateDomainException extends RuntimeException {
    public DuplicateDomainException(String message, String name) { super(message + ": " + name); }
}
