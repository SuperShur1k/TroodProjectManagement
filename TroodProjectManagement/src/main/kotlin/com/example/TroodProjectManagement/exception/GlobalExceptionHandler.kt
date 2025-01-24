package com.example.TroodProjectManagement.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Global exception handler to manage validation and JSON parsing errors.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handles validation exceptions when request parameters violate constraints.
     *
     * @param ex The thrown ConstraintViolationException.
     * @return A response with error messages and HTTP 400 status.
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException): ResponseEntity<Map<String, String>> {
        val errors = ex.constraintViolations.associate { it.propertyPath.toString() to it.message }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles JSON parsing errors, such as missing required fields.
     *
     * @param ex The thrown HttpMessageNotReadableException.
     * @return A response with an appropriate error message and HTTP 400 status.
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException::class)
    fun handleJsonParseError(ex: org.springframework.http.converter.HttpMessageNotReadableException): ResponseEntity<Map<String, String>> {
        val cause = ex.cause
        return if (cause is MissingKotlinParameterException) {
            val missingField = cause.parameter.name ?: "Unknown field"
            ResponseEntity.status(400).body(mapOf("error" to "Missing required field: $missingField"))
        } else {
            ResponseEntity.status(400).body(mapOf("error" to "Invalid JSON format or missing required fields"))
        }
    }
}