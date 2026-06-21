package com.mariluz.usuario.controller;

import com.mariluz.usuario.dto.ActualizarDireccionRequest;
import com.mariluz.usuario.dto.ErrorResponse;
import com.mariluz.usuario.dto.crear_direcion.DireccionRequest;
import com.mariluz.usuario.dto.crear_direcion.DireccionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "Direcciones", description = "Gestión de la dirección del usuario autenticado")
public interface DireccionApi {

    // 1. crear direccion usuario
    @Operation(summary = "Crear dirección", description = "Crea la dirección del usuario autenticado. Un usuario solo puede tener una dirección.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dirección creada correctamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (campos obligatorios faltantes o formato incorrecto).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "region": "la region es obligatoria" },
                        "message": "Datos inválidos",
                        "status": 400,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido/expirado, o el usuario ya tiene una dirección registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "error": "Se requiere token de autenticación" },
                        "message": "No autenticado",
                        "status": 401,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "error": "Error inesperado" },
                        "message": "Error interno del servidor",
                        "status": 500,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """)))
    })
    public ResponseEntity<DireccionResponse> agregarDireccion(
            @Valid DireccionRequest request);

    // 2. obtener direccion usuario
    @Operation(summary = "Obtener dirección", description = "Devuelve la dirección del usuario autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dirección encontrada."),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o expirado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "error": "Se requiere token de autenticación" },
                        "message": "No autenticado",
                        "status": 401,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "El usuario no tiene una dirección registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": null,
                        "message": "No se encontró dirección para este usuario",
                        "status": 404,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "error": "Error inesperado" },
                        "message": "Error interno del servidor",
                        "status": 500,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """)))
    })
    public ResponseEntity<DireccionResponse> obtenerDireccion();

    // 3. actualizar direccion
    @Operation(summary = "Actualizar dirección", description = "Actualiza la dirección existente del usuario autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dirección actualizada correctamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (campos obligatorios faltantes o formato incorrecto).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "id": "El id debe ser un número positivo" },
                        "message": "Datos inválidos",
                        "status": 400,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o expirado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "error": "Se requiere token de autenticación" },
                        "message": "No autenticado",
                        "status": 401,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "El usuario no tiene una dirección registrada para actualizar.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": null,
                        "message": "No se encontró dirección para este usuario",
                        "status": 404,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "endpoint": "/user/direccion",
                        "errors": { "error": "Error inesperado" },
                        "message": "Error interno del servidor",
                        "status": 500,
                        "timeStamp": "2026-06-12T05:11:58"
                    }
                    """)))
    })
    public ResponseEntity<DireccionResponse> actualizarDireccion(
            @Valid ActualizarDireccionRequest request);
}
