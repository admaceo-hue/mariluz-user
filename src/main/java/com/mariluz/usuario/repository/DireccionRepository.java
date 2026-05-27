package com.mariluz.usuario.repository;

import com.mariluz.usuario.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    // encontrar direccion por usuario
    public Direccion findByUsuarioId(String usuarioId);

    // validar existencia direccion
    public boolean existsByUsuarioId(String usuarioId);
}
