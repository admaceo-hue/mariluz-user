package com.mariluz.usuario.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.mariluz.usuario.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    public Direccion findByUsuarioId(String usuarioId);

}