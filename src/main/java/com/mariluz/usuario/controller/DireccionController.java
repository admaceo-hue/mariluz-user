package com.mariluz.usuario.controller;

import com.mariluz.usuario.dto.*;
import com.mariluz.usuario.dto.crear_direcion.*;
import com.mariluz.usuario.service.DireccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class DireccionController {

    @Autowired
    private DireccionService service;

    // 1. crear direccion usuario
    @PostMapping("/direccion")
    public ResponseEntity<DireccionResponse> agregarDireccion(
        @Valid @RequestBody DireccionRequest request
    ) {
        // Llama al service para guardar y obtener la respuesta
        return ResponseEntity.status(HttpStatus.CREATED).body(
            service.creaDireccion(request)
        );
    }

    // 2. obtener direccion usuario
    @GetMapping("/direccion")
    public ResponseEntity<DireccionResponse> obtenerDireccion() {
        // Devolvemos la respuesta con un estado 200 OK
        return ResponseEntity.ok(service.obtenerDireccionUsuario());
    }

    // 3. actualizar direccion
    @PutMapping("/direccion")
    public ResponseEntity<DireccionResponse> actualizarDireccion(
        @Valid @RequestBody ActualizarDireccionRequest request
    ) {
        return ResponseEntity.ok(service.actualizarDireccion(request));
    }

    // 4. mostrar historial de compras
}
