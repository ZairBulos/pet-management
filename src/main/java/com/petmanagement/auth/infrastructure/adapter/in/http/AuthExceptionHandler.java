package com.petmanagement.auth.infrastructure.adapter.in.http;

import com.petmanagement.auth.domain.exception.OwnerNotFoundException;
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

}
