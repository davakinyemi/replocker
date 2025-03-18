package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(UUID adminId) {}
    public AdminNotFoundException(String message) { super(message); }
}
