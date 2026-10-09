package com.petmanagement.pets.infrastructure.adapter.in.http;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = PetController.class)
class PetExceptionHandler {
}
