package com.mariluz.usuario.dto.crear_direcion;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DireccionRequest {
  @NotBlank(message = "la region es obligatoria")
  private String region;
    @NotBlank(message = "la comuna es obligatoria")
    private String comuna;
    @NotBlank(message = "la calle es obligatoria")
    private String calle;
    @NotBlank(message = "el numero es obligatorio")
    private String numero;
    
    
}
