package com.example.TroodProjectManagement.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException): ResponseEntity<Map<String, String>> {
        val errors = ex.constraintViolations.associate { it.propertyPath.toString() to it.message }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}
