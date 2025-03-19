package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message, UUID userId) { super(message + ": " + userId); }
}
