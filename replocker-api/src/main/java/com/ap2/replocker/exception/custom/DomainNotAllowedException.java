package com.ap2.replocker.exception.custom;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class DomainNotAllowedException extends RuntimeException {
    public DomainNotAllowedException(String domain) { super(domain); }
}
