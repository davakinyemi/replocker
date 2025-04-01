package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class AccessTokenNotFoundException extends RuntimeException {
    public AccessTokenNotFoundException(UUID tokenId) { super(tokenId.toString()); }
}
