package com.ap2.replocker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    BAD_CREDENTIALS(304, FORBIDDEN, "One or more access credentials incorrect"),
    INVALID_FILE_TYPE(400, FORBIDDEN, "Invalid file type, provide csv or excel file format"),
    ADMIN_NOT_FOUND(404, NOT_FOUND, "Admin not found"),
    USER_NOT_FOUND(404, NOT_FOUND, "User not found"),
    DOMAIN_NOT_FOUND(404, NOT_FOUND, "Domain not found"),
    ACCESS_REQUEST_NOT_FOUND(404, NOT_FOUND, "Access request not found"),
    INVALID_TOKEN(401, UNAUTHORIZED, "Invalid token"),
    TOKEN_GENERATION_FAILURE(403, FORBIDDEN, "Token generation failure"),
    COLLECTION_NOT_FOUND(404, NOT_FOUND, "Collection not found"),
    REPORT_NOT_FOUND(404, NOT_FOUND, "Report not found"),
    NOTIFICATION_NOT_FOUND(404, NOT_FOUND, "Notification not found"),
    ACCESS_TOKEN_NOT_FOUND(404, NOT_FOUND, "Access token not found"),
    DUPLICATE_DOMAIN_NAME(409, FORBIDDEN, "Duplicate domain name"),
    DUPLICATE_COLLECTION_NAME(409, FORBIDDEN, "Duplicate collection name"),
    DUPLICATE_REPORT_NAME(409, FORBIDDEN, "Duplicate report name"),
    DUPLICATE_REQUEST_ACCESS(409, FORBIDDEN, "Duplicate request access"),
    KEYCLOAK_SERVER_ERROR(500, INTERNAL_SERVER_ERROR, "Keycloak server error"),
    BUSINESS_RULE_ERROR(500, FORBIDDEN, "Business rule error"),
    EMAIL_DOMAIN_NOT_PERMITTED(400, FORBIDDEN, "Email domain not permitted"),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }

}
