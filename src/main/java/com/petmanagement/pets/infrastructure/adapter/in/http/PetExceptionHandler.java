package com.petmanagement.pets.infrastructure.adapter.in.http;

import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.shared.domain.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = PetController.class)
class PetExceptionHandler {

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

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handlePetNotFound(
            PetNotFoundException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(
                "PET_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

}
