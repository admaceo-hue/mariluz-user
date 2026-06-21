package com.mariluz.usuario.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mariluz.usuario.dto.ActualizarDireccionRequest;
import com.mariluz.usuario.dto.crear_direcion.DireccionRequest;
import com.mariluz.usuario.dto.crear_direcion.DireccionResponse;
import com.mariluz.usuario.exception.ResourceNotFoundException;
import com.mariluz.usuario.exception.UnauthorizedOperationException;
import com.mariluz.usuario.security.JwtUtil;
import com.mariluz.usuario.service.DireccionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(DireccionController.class)
@AutoConfigureMockMvc(addFilters = false) // desactiva filtro JWT y seguridad para ejecutar el test
public class DireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper; 

    @MockitoBean
    private DireccionService service;

    @MockitoBean
    private JwtUtil jwtUtil; 

    // metodo de apoyo para construir un request valido
    private DireccionRequest direccionRequestValido() {
        DireccionRequest request = new DireccionRequest();
        request.setRegion("Region Metropolitana");
        request.setComuna("Santiago");
        request.setCalle("Av. Siempre Viva");
        request.setNumero("742");
        return request;
    }

    private DireccionResponse direccionResponse() {
        return DireccionResponse.builder()
            .id(1)
            .region("Region Metropolitana")
            .comuna("Santiago")
            .calle("Av. Siempre Viva")
            .numero("742")
            .build();
    }

    //  1. CREAR DIRECCION 
    // 201
    @Test
    public void testCrearDireccion() throws Exception {
        DireccionRequest request = direccionRequestValido();

        when(service.creaDireccion(request)).thenReturn(direccionResponse());

        mockMvc
            .perform(
                post("/user/direccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated());
    }

    // 400
    @Test
    public void testCrearDireccionBadRequest() throws Exception {
        DireccionRequest request = new DireccionRequest();
        request.setRegion("");
        request.setComuna("");
        request.setCalle("");
        request.setNumero("");

        mockMvc
            .perform(
                post("/user/direccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());
    }

    // 401
    @Test
    public void testCrearDireccionYaTieneDireccion() throws Exception {
        DireccionRequest request = direccionRequestValido();

        when(service.creaDireccion(request)).thenThrow(
            new UnauthorizedOperationException(
                "El usuario ya tiene una dirección registrada. No puede crear otra."
            )
        );

        mockMvc
            .perform(
                post("/user/direccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isUnauthorized());
    }

    //  2. OBTENER DIRECCION 
    // 200
    @Test
    public void testObtenerDireccion() throws Exception {
        when(service.obtenerDireccionUsuario()).thenReturn(direccionResponse());

        mockMvc
            .perform(get("/user/direccion"))
            .andExpect(status().isOk());
    }

    // 404
    @Test
    public void testObtenerDireccionNotFound() throws Exception {
        when(service.obtenerDireccionUsuario()).thenThrow(
            new ResourceNotFoundException(
                "No se encontró dirección para este usuario"
            )
        );

        mockMvc
            .perform(get("/user/direccion"))
            .andExpect(status().isNotFound());
    }

    // -------------- 3. ACTUALIZAR DIRECCION --------------
    // 200
    @Test
    public void testActualizarDireccion() throws Exception {
        ActualizarDireccionRequest request = new ActualizarDireccionRequest();
        request.setId(1);
        request.setRegion("Valparaiso");
        request.setComuna("Vina del Mar");
        request.setCalle("Calle Nueva");
        request.setNumero("100");

        when(service.actualizarDireccion(request)).thenReturn(
            DireccionResponse.builder()
                .id(1)
                .region("Valparaiso")
                .comuna("Vina del Mar")
                .calle("Calle Nueva")
                .numero("100")
                .build()
        );

        mockMvc
            .perform(
                put("/user/direccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk());
    }

    // 400
    @Test
    public void testActualizarDireccionBadRequest() throws Exception {
        ActualizarDireccionRequest request = new ActualizarDireccionRequest();
        request.setId(null);
        request.setRegion("");
        request.setComuna("");
        request.setCalle("");
        request.setNumero("");

        mockMvc
            .perform(
                put("/user/direccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());
    }

    // 404
    @Test
    public void testActualizarDireccionNotFound() throws Exception {
        ActualizarDireccionRequest request = new ActualizarDireccionRequest();
        request.setId(1);
        request.setRegion("Valparaiso");
        request.setComuna("Vina del Mar");
        request.setCalle("Calle Nueva");
        request.setNumero("100");

        when(service.actualizarDireccion(request)).thenThrow(
            new ResourceNotFoundException(
                "No se encontró dirección para este usuario"
            )
        );

        mockMvc
            .perform(
                put("/user/direccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNotFound());
    }
}
