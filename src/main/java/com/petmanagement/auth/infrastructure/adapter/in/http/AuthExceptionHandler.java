package com.petmanagement.auth.infrastructure.adapter.in.http;

import com.petmanagement.auth.domain.exception.*;
import com.petmanagement.shared.domain.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = AuthController.class)
class AuthExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "VALIDATION_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(OwnerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleOwnerNotFound(
            OwnerNotFoundException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "OWNER_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ActiveAuthenticationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleActiveAuthenticationNotFound(
            ActiveAuthenticationNotFoundException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "ACTIVE_AUTHENTICATION_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AuthenticationAlreadyUsedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse handleAuthenticationAlreadyUsed(
            AuthenticationAlreadyUsedException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "AUTHENTICATION_ALREADY_USED",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AuthenticationExpiredException.class)
    @ResponseStatus(HttpStatus.GONE)
    ErrorResponse handleAuthenticationExpired(
            AuthenticationExpiredException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "AUTHENTICATION_EXPIRED",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidAuthenticationCodeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ErrorResponse handleInvalidAuthenticationCode(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "INVALID_AUTHENTICATION_CODE",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({
            SessionNotFoundException.class,
            SessionReuseDetectedException.class,
            SessionRevokedException.class,
            SessionExpiredException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ErrorResponse handleInvalidSession(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "INVALID_SESSION",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

}
