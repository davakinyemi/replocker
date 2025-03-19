package com.ap2.replocker.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    BAD_CREDENTIALS(304, FORBIDDEN, "One or more access credentials incorrect"),
    INVALID_FILE_TYPE(400, FORBIDDEN, "Invalid file type, provide csv or excel file format"),
    ADMIN_NOT_FOUND(404, FORBIDDEN, "Admin not found"),
    USER_NOT_FOUND(404, FORBIDDEN, "User not found"),
    ACCESS_REQUEST_NOT_FOUND(404, FORBIDDEN, "Access request not found"),
    INVALID_TOKEN(401, FORBIDDEN, "Invalid token"),
    COLLECTION_NOT_FOUND(404, FORBIDDEN, "Collection not found"),
    DUPLICATE_DOMAIN_NAME(409, FORBIDDEN, "Duplicate domain name"),
    DUPLICATE_COLLECTION_NAME(409, FORBIDDEN, "Duplicate collection name"),
    DUPLICATE_REPORT_NAME(409, FORBIDDEN, "Duplicate report name"),
    ;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
    }
}
