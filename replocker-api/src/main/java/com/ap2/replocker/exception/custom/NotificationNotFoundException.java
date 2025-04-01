package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(UUID accessRequestId) { super(accessRequestId.toString()); }
}
