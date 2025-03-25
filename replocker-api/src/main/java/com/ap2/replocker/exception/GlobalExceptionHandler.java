package com.ap2.replocker.exception;

import com.ap2.replocker.exception.custom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Common handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
        Set<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());

        return ResponseEntity.badRequest().body(
                ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
        );
        /* Set<String> errors = new HashSet<>();
        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                ); */
    }

    // Generic handler for business exceptions
    @ExceptionHandler({
        OperationNotPermittedException.class,
        InvalidFileTypeException.class,
        AdminNotFoundException.class,
        UserNotFoundException.class,
        AccessRequestNotFoundException.class,
        CollectionNotFoundException.class,
        DuplicateDomainException.class,
        DuplicateCollectionException.class,
        DuplicateReportException.class
    })
    public ResponseEntity<ExceptionResponse> handleBusinessExceptions(RuntimeException e) {
        BusinessErrorCodes errorCodes = this.resolveErrorCode(e);
        return ResponseEntity.status(errorCodes.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(errorCodes.getCode())
                        .error(errorCodes.getDescription())
                        .build()
                );
    }

    private BusinessErrorCodes resolveErrorCode(RuntimeException e) {
        if (e instanceof InvalidFileTypeException) return BusinessErrorCodes.INVALID_FILE_TYPE;
        else if (e instanceof AdminNotFoundException) return BusinessErrorCodes.ADMIN_NOT_FOUND;
        else if (e instanceof UserNotFoundException) return BusinessErrorCodes.USER_NOT_FOUND;
        else if (e instanceof AccessRequestNotFoundException) return BusinessErrorCodes.ACCESS_REQUEST_NOT_FOUND;
        else if (e instanceof CollectionNotFoundException) return BusinessErrorCodes.COLLECTION_NOT_FOUND;
        else if (e instanceof DuplicateDomainException) return BusinessErrorCodes.DUPLICATE_DOMAIN_NAME;
        else if (e instanceof DuplicateCollectionException) return BusinessErrorCodes.DUPLICATE_COLLECTION_NAME;
        else if (e instanceof DuplicateReportException) return BusinessErrorCodes.DUPLICATE_REPORT_NAME;
        return BusinessErrorCodes.NO_CODE;
    }

    // Special case handlers
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidToken(InvalidTokenException e) {
        return ResponseEntity.status(FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.INVALID_TOKEN.getCode())
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ExceptionResponse> handleKeycloakException(KeycloakException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.KEYCLOAK_SERVER_ERROR.getCode())
                        .error(e.getMessage())
                        .build()
                );
    }

    // Global fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        LOGGER.error("Unhandled exception: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(
                ExceptionResponse.builder()
                        .error("Internal Server Error")
                        .build()
        );
    }

    /*@ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(e.getMessage())
                                .build()
                );
    }*/

    /*@ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidFileTypeException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INVALID_FILE_TYPE.ordinal())
                                .error(e.getMessage())
                                .build()
                );
    }*/

    /*@ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(AdminNotFoundException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ADMIN_NOT_FOUND.ordinal())
                                .businessExceptionDescription(e.getMessage())
                                .error(e.getMessage())
                                .build()
                );
    }*/

    /*@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.USER_NOT_FOUND.ordinal())
                                .businessExceptionDescription(e.getMessage())
                                .error(e.getMessage())
                                .build()
                );
    }*/

    /*@ExceptionHandler(AccessRequestNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(AccessRequestNotFoundException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ACCESS_REQUEST_NOT_FOUND.ordinal())
                                .businessExceptionDescription(e.getMessage())
                                .error(e.getMessage())
                                .build()
                );
    }*/

    /*@ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidTokenException e) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INVALID_TOKEN.ordinal())
                                .error(e.getMessage())
                                .build()
                );
    }*/

    /*@ExceptionHandler(CollectionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(CollectionNotFoundException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.COLLECTION_NOT_FOUND.ordinal())
                        .businessExceptionDescription(e.getMessage())
                        .error(e.getMessage())
                        .build()
                );
    }*/

    /*@ExceptionHandler(DuplicateDomainException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateDomainException e) {
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.DUPLICATE_DOMAIN_NAME.ordinal())
                        .businessExceptionDescription(e.getMessage())
                        .error(e.getMessage())
                        .build());
    }*/

    /*@ExceptionHandler(DuplicateCollectionException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateCollectionException e) {
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.DUPLICATE_COLLECTION_NAME.ordinal())
                        .businessExceptionDescription(e.getMessage())
                        .error(e.getMessage())
                        .build());
    }*/

    /*@ExceptionHandler(DuplicateReportException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateReportException e) {
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.DUPLICATE_REPORT_NAME.ordinal())
                        .businessExceptionDescription(e.getMessage())
                        .error(e.getMessage())
                        .build());
    }*/

    /*@ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ExceptionResponse> handleException(KeycloakException e) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.KEYCLOAK_EXCEPTION.ordinal())
                        .businessExceptionDescription(e.getMessage())
                        .error(e.getMessage())
                        .build());
    }*/

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        LOGGER.error("Unhandled e occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessExceptionDescription("Internal error, contact the admin")
                                .error(e.getMessage())
                                .build()
                );
    }*/
}
