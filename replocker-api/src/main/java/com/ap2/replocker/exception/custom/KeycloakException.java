package com.ap2.replocker.exception.custom;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class KeycloakException extends RuntimeException {
    public KeycloakException(String message) { super(message); }
    public KeycloakException(String message, Throwable cause) { super(message, cause); }
}
