package com.ap2.replocker.exception.custom;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String adminId) {}
    public AdminNotFoundException(String adminId, String message) { super(message); }
}
