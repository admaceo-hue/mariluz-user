package com.mariluz.usuario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mariluz.usuario.dto.crear_direcion.DireccionRequest;
import com.mariluz.usuario.dto.crear_direcion.DireccionResponse;
import com.mariluz.usuario.exception.UnauthorizedOperationException;
import com.mariluz.usuario.model.Direccion;
import com.mariluz.usuario.model.User;
import com.mariluz.usuario.repository.DireccionRepository;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository repo;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new UnauthorizedOperationException("No hay un usuario autenticado o el token es inválido");
        }
        return user;
    }

    public DireccionResponse creaDireccion(DireccionRequest request) {
        User currentUser = getCurrentUser();
        
        Direccion nuevaDireccion = Direccion.builder()
                .region(request.getRegion())
                .comuna(request.getComuna())
                .calle(request.getCalle())
                .numero(request.getNumero())
                .usuarioId(currentUser.getId())
                .build();

        Direccion direccionGuardada = repo.save(nuevaDireccion);

        return mapToResponse(direccionGuardada);
    }

    public DireccionResponse obtenerDireccionUsuario() {
        User currentUser = getCurrentUser();
        
        Direccion dir = repo.findByUsuarioId(currentUser.getId());
        
        if (dir == null) {
            throw new UnauthorizedOperationException("No se encontró dirección para este usuario");
        }

        return mapToResponse(dir);
    }

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