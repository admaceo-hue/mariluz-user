package com.mariluz.usuario.exception;

import com.mariluz.usuario.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler token expirado
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredTokenException(
        ExpiredJwtException ex,
        HttpServletRequest request
    ) {
        Map<String, String> error = Map.of(
            "error",
            "El token ha expirado. Por favor, inicie sesión nuevamente."
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Token expirado")
                .errors(error)
                .endpoint(request.getRequestURI())
                .build()
        );
    }

    // Handler error generico de JWT
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleGenericJwtError(
        JwtException ex,
        HttpServletRequest request
    ) {
        Map<String, String> error = Map.of(
            "error",
            "El token proporcionado es inválido o está corrupto."
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de token")
                .errors(error)
                .endpoint(request.getRequestURI())
                .build()
        );
    }

    /*
        Handler MethodNotSupported Exception
        -> si el usuario usa GET en vez del metodo del endpoint arroja este error
    */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
        HttpRequestMethodNotSupportedException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
            ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message("Método no soportado")
                .errors(Map.of("error", ex.getMessage()))
                .endpoint(request.getRequestURI())
                .build()
        );
    }

    // Captura el 404 (No encontrado) cuando falta la dirección o el email
    @ExceptionHandler({
        ResourceNotFoundException.class,
        EmailNotFoundException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFound(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            null,
            request.getRequestURI()
        );
    }

    // Captura el 401 (No autorizado) para permisos
    @ExceptionHandler({
        UnauthorizedOperationException.class,
        InvalidCredentialsException.class,
    })
    public ResponseEntity<ErrorResponse> handleUnauthorized(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        return buildErrorResponse(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            null,
            request.getRequestURI()
        );
    }

    // Captura el 400 (Solicitud Incorrecta) cuando el email está repetido
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
        EmailAlreadyInUseException ex,
        HttpServletRequest request
    ) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            null,
            request.getRequestURI()
        );
    }

    // Captura errores de validación de Spring (@Valid en los campos obligatorios)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            });
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Datos inválidos",
            fieldErrors,
            request.getRequestURI()
        );
    }

    // Método de soporte para armar el JSON estructurado final
    private ResponseEntity<ErrorResponse> buildErrorResponse(
        HttpStatus status,
        String message,
        Map<String, String> errors,
        String endpoint
    ) {
        ErrorResponse response = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(status.value())
            .message(message)
            .errors(errors)
            .endpoint(endpoint)
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
            "Datos inválidos",
            error,
            request.getRequestURI()
        );
    }
}
