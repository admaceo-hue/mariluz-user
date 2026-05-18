package com.mariluz.usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mariluz.usuario.Service.DireccionService;
import com.mariluz.usuario.dto.crear_direcion.DireccionRequest;
import com.mariluz.usuario.dto.crear_direcion.DireccionResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    
    @PostMapping("/direccion")
    public ResponseEntity<DireccionResponse> agregarDireccion(@Valid @RequestBody DireccionRequest request) {
        // Llama al service para guardar y obtener la respuesta
        DireccionResponse response = direccionService.creaDireccion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @GetMapping("/direccion")
    public ResponseEntity<DireccionResponse> obtenerDireccion() {
       
        DireccionResponse response = direccionService.obtenerDireccionUsuario();
        // Devolvemos la respuesta con un estado 200 OK(bruno revisa esto)
        return ResponseEntity.ok(response);
    }
}