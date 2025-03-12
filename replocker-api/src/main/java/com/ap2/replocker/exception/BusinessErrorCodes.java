package com.ap2.replocker.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    BAD_CREDENTIALS(304, FORBIDDEN, "One or more access credentials incorrect"),
    ;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
    }
}
