package com.mariluz.usuario.dto.crear_direcion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionResponse {

    private Integer id;

    private String region;

    private String comuna;

    private String calle;

    private String numero;
}
