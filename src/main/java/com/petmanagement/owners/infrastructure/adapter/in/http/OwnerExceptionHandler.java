package com.petmanagement.owners.infrastructure.adapter.in.http;

import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.shared.domain.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = OwnerController.class)
class OwnerExceptionHandler {

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

    @ExceptionHandler(OwnerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse handleOwnerAlreadyExists(
            OwnerAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "OWNER_ALREADY_EXISTS",
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
