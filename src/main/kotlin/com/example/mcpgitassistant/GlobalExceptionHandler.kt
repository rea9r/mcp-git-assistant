package com.example.mcpgitassistant

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
	@ExceptionHandler(IllegalArgumentException::class)
	fun handleInvalidArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> =
		ResponseEntity.badRequest().body(ErrorResponse(message = ex.message ?: "Invalid argument"))
}

data class ErrorResponse(val message: String)
