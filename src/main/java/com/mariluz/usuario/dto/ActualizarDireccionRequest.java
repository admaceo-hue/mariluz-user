package com.mariluz.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActualizarDireccionRequest {

    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id debe ser un número positivo")
    private Integer id;

    @NotBlank(message = "La región no puede ser nula")
    private String region;

    @NotBlank(message = "La comuna no puede ser nula")
    private String comuna;

    @NotBlank(message = "La calle no puede ser nula")
    private String calle;

    @NotBlank(message = "El número no puede ser nulo")
    private String numero;
}
