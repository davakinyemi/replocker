package com.ap2.replocker.exception.custom;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException() { }

    public OperationNotPermittedException(String message) {
        super(message);
    }
}
