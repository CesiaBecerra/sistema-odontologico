package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;
import com.digitaldentic.sistemaodontologico.Entity.RolEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Service.PacienteService;
import com.digitaldentic.sistemaodontologico.Service.UsuarioService;
import com.digitaldentic.sistemaodontologico.controller.PacienteController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PacienteController.class)
public class PacienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PacienteService pacienteService;
    @MockBean private UsuarioService usuarioService;

    @Test
    void testListarPacientes_ComoAdmin_RetornaLista() throws Exception {
        UsuarioEntity admin = new UsuarioEntity();
        admin.setRol(RolEntity.ADMIN);
        given(usuarioService.buscarPorEmail(any())).willReturn(admin);
        given(pacienteService.listar()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/pacientes").with(user("admin@test.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("pacientes/lista"));
    }

    @Test
    void testNuevoPaciente_RetornaFormulario() throws Exception {
        mockMvc.perform(get("/pacientes/nuevo").with(user("admin@test.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("pacientes/form"));
    }

    @Test
    void testGuardarPaciente_Exito() throws Exception {
        mockMvc.perform(post("/pacientes/guardar")
                        .with(user("admin@test.com")).with(csrf())
                        .flashAttr("paciente", new PacienteEntity()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }

    @Test
    void testGuardarPaciente_ErrorDniDuplicado() throws Exception {
        // Obligamos al servicio a lanzar una RuntimeException para probar el catch del controlador
        doThrow(new RuntimeException("Error: DNI duplicado")).when(pacienteService).guardar(any());

        mockMvc.perform(post("/pacientes/guardar")
                        .with(user("admin@test.com")).with(csrf())
                        .flashAttr("paciente", new PacienteEntity()))
                .andExpect(status().isOk())
                .andExpect(view().name("pacientes/form"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void testBuscarPacientePorNombre() throws Exception {
        mockMvc.perform(get("/pacientes/buscar").param("nombre", "Juan")
                        .with(user("admin@test.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("pacientes/lista"));

        verify(pacienteService).buscarPorNombre("Juan");
    }

    @Test
    void testEliminarPaciente() throws Exception {
        mockMvc.perform(get("/pacientes/eliminar/1")
                        .with(user("admin@test.com")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));

        verify(pacienteService).eliminar(1L);
    }
}