package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class AccessRequestNotFoundException extends RuntimeException {
    public AccessRequestNotFoundException(String message, UUID requestId) {
        super("Access request not found: " + requestId);
    }
}
