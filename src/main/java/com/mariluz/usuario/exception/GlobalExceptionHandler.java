package com.mariluz.usuario.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura el 404 (No encontrado) cuando falta la dirección o el email
    @ExceptionHandler({ResourceNotFoundException.class, EmailNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null);
    }

    // Captura el 401 (No autorizado) para permisos o contraseñas malas
    @ExceptionHandler({UnauthorizedOperationException.class, InvalidCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), null);
    }

    // Captura el 400 (Solicitud Incorrecta) cuando el email está repetido
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(EmailAlreadyInUseException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null);
    }

    // Captura errores de validación de Spring (@Valid en los campos obligatorios)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", "Datos inválidos", fieldErrors);
    }

    // Método de soporte para armar el JSON estructurado final
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, String errorType, String message, Map<String, String> errors) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(errorType)
                .message(message)
                .errors(errors)
                .build();
                
        return new ResponseEntity<>(response, status);



        
    }
    // Validacion Parseo del json
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        Map<String, String> error = Map.of(
            "error",
            "Revise el formato de los campos enviados."
        );

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Error en la solicitud",
            "Datos inválidos",
            error
        );
    }
}
