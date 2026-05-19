package com.mariluz.usuario.service;

import com.mariluz.usuario.dto.ActualizarDireccionRequest;
import com.mariluz.usuario.dto.crear_direcion.DireccionRequest;
import com.mariluz.usuario.dto.crear_direcion.DireccionResponse;
import com.mariluz.usuario.exception.ResourceNotFoundException;
import com.mariluz.usuario.exception.UnauthorizedOperationException; // <-- Importamos el error 404
import com.mariluz.usuario.model.Direccion;
import com.mariluz.usuario.model.User;
import com.mariluz.usuario.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository repo;

    private User getCurrentUser() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new UnauthorizedOperationException(
                "No hay un usuario autenticado o el token es inválido"
            );
        }
        return user;
    }

    public DireccionResponse creaDireccion(DireccionRequest request) {
        User user = getCurrentUser();

        // 1. Validamos si el usuario ya cuenta con una dirección registrada(para evitar duplicados)
        if (repo.existsByUsuarioId(user.getId())) {
            // Si ya existe, lanzamos la excepción para bloquear el flujo y no duplicar datos
            throw new UnauthorizedOperationException(
                "El usuario ya tiene una dirección registrada. No puede crear otra."
            );
        }

        Direccion nuevaDireccion = Direccion.builder()
            .region(request.getRegion())
            .comuna(request.getComuna())
            .calle(request.getCalle())
            .numero(request.getNumero())
            .usuarioId(user.getId())
            .build();

        Direccion direccionGuardada = repo.save(nuevaDireccion);

        return mapToResponse(direccionGuardada);
    }

    public DireccionResponse obtenerDireccionUsuario() {
        User currentUser = getCurrentUser();

        Direccion dir = repo.findByUsuarioId(currentUser.getId());

        // Corregido: Si no existe el registro, es un error 404 (ResourceNotFoundException)
        if (dir == null) {
            throw new ResourceNotFoundException(
                "No se encontró dirección para este usuario"
            );
        }

        return mapToResponse(dir);
    }

    // 3. actualizar direccion
    public DireccionResponse actualizarDireccion(
        ActualizarDireccionRequest request
    ) {
        // 1. obtener usuario
        User user = getCurrentUser();
        // 2. validar existencia de direccion
        if (!repo.existsByUsuarioId(user.getId())) {
            throw new ResourceNotFoundException(
                "No se encontró dirección para este usuario"
            );
        }
        // 3. actualizar y retornar
        Direccion dir = repo.save(
            Direccion.builder()
                .id(request.getId())
                .region(request.getRegion())
                .comuna(request.getComuna())
                .calle(request.getCalle())
                .numero(request.getNumero())
                .usuarioId(user.getId())
                .build()
        );

        return DireccionResponse.builder()
            .id(dir.getId())
            .region(dir.getRegion())
            .comuna(dir.getComuna())
            .calle(dir.getCalle())
            .numero(dir.getNumero())
            .build();
    }

    // 4. mostrar historial de compras

    // Método privado para evitar repetir código al convertir Entidad a DTO
    private DireccionResponse mapToResponse(Direccion dir) {
        return DireccionResponse.builder()
            .id(dir.getId())
            .region(dir.getRegion())
            .comuna(dir.getComuna())
            .calle(dir.getCalle())
            .numero(dir.getNumero())
            .build();
    }
}
