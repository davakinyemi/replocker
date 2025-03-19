package com.ap2.replocker.exception;

import com.ap2.replocker.exception.custom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors()
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
                );
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidFileTypeException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INVALID_FILE_TYPE.ordinal())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(AdminNotFoundException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ADMIN_NOT_FOUND.ordinal())
                                .businessExceptionDescription(exception.getMessage())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.USER_NOT_FOUND.ordinal())
                                .businessExceptionDescription(exception.getMessage())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(AccessRequestNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(AccessRequestNotFoundException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ACCESS_REQUEST_NOT_FOUND.ordinal())
                                .businessExceptionDescription(exception.getMessage())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidTokenException exception) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INVALID_TOKEN.ordinal())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(CollectionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(CollectionNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.COLLECTION_NOT_FOUND.ordinal())
                        .businessExceptionDescription(exception.getMessage())
                        .error(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(DuplicateDomainException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateDomainException exception) {
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.DUPLICATE_DOMAIN_NAME.ordinal())
                        .businessExceptionDescription(exception.getMessage())
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateCollectionException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateCollectionException exception) {
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.DUPLICATE_COLLECTION_NAME.ordinal())
                        .businessExceptionDescription(exception.getMessage())
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateReportException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateReportException exception) {
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.DUPLICATE_REPORT_NAME.ordinal())
                        .businessExceptionDescription(exception.getMessage())
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        LOGGER.error("Unhandled exception occurred: {}", exception.getMessage(), exception);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessExceptionDescription("Internal error, contact the admin")
                                .error(exception.getMessage())
                                .build()
                );
    }
}
