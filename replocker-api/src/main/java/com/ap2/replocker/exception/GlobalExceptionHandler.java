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

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
    }

    // Generic handler for business exceptions
    @ExceptionHandler({
        OperationNotPermittedException.class,
        InvalidFileTypeException.class,
        AdminNotFoundException.class,
        UserNotFoundException.class,
        DomainNotFoundException.class,
        AccessRequestNotFoundException.class,
        CollectionNotFoundException.class,
        DuplicateDomainException.class,
        DuplicateCollectionException.class,
        DuplicateReportException.class,
        DuplicateRequestException.class,
        DomainNotAllowedException.class,
    })
    public ResponseEntity<ExceptionResponse> handleBusinessExceptions(RuntimeException e) {
        BusinessErrorCodes errorCodes = this.resolveErrorCode(e);
        return ResponseEntity.status(errorCodes.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(errorCodes.getCode())
                        .error(errorCodes.getDescription() + ": " + e.getMessage())
                        .build()
                );
    }

    private BusinessErrorCodes resolveErrorCode(RuntimeException e) {
        if (e instanceof InvalidFileTypeException) return BusinessErrorCodes.INVALID_FILE_TYPE;
        else if (e instanceof AdminNotFoundException) return BusinessErrorCodes.ADMIN_NOT_FOUND;
        else if (e instanceof UserNotFoundException) return BusinessErrorCodes.USER_NOT_FOUND;
        else if (e instanceof DomainNotFoundException) return BusinessErrorCodes.DOMAIN_NOT_FOUND;
        else if (e instanceof AccessRequestNotFoundException) return BusinessErrorCodes.ACCESS_REQUEST_NOT_FOUND;
        else if (e instanceof CollectionNotFoundException) return BusinessErrorCodes.COLLECTION_NOT_FOUND;
        else if (e instanceof DuplicateDomainException) return BusinessErrorCodes.DUPLICATE_DOMAIN_NAME;
        else if (e instanceof DuplicateCollectionException) return BusinessErrorCodes.DUPLICATE_COLLECTION_NAME;
        else if (e instanceof DuplicateReportException) return BusinessErrorCodes.DUPLICATE_REPORT_NAME;
        else if (e instanceof DuplicateRequestException) return BusinessErrorCodes.DUPLICATE_REQUEST_ACCESS;
        else if (e instanceof TokenGenerationException) return BusinessErrorCodes.TOKEN_GENERATION_FAILURE;
        else if (e instanceof DomainNotAllowedException) return BusinessErrorCodes.EMAIL_DOMAIN_NOT_PERMITTED;
        return BusinessErrorCodes.NO_CODE;
    }

    // Special case handlers
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidToken(InvalidTokenException e) {
        return ResponseEntity.status(FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.INVALID_TOKEN.getCode())
                        .error(BusinessErrorCodes.INVALID_TOKEN.getDescription() + ": " + e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ExceptionResponse> handleKeycloakException(KeycloakException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.KEYCLOAK_SERVER_ERROR.getCode())
                        .error(BusinessErrorCodes.KEYCLOAK_SERVER_ERROR.getDescription() + ": " + e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessRuleException(BusinessRuleException e) {
        return ResponseEntity.status(FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.BUSINESS_RULE_ERROR.getCode())
                        .error(BusinessErrorCodes.BUSINESS_RULE_ERROR.getDescription() + ": " + e.getMessage())
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
}
