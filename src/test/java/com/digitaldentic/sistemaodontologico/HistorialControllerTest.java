package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;
import com.digitaldentic.sistemaodontologico.Service.HistorialService;
import com.digitaldentic.sistemaodontologico.Service.PacienteService;
import com.digitaldentic.sistemaodontologico.controller.HistorialController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistorialController.class)
class HistorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistorialService historialService;

    @MockBean
    private PacienteService pacienteService;

    // =========================
    // VER HISTORIAL
    // =========================
    @Test
    void testVerHistorial_Ok() throws Exception {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        given(pacienteService.buscarPorId(1L)).willReturn(paciente);
        given(historialService.buscarPorPaciente(1L)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/historial/paciente/1").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("historial/lista"));
    }

    @Test
    void testVerHistorial_PacienteNull() throws Exception {

        given(pacienteService.buscarPorId(1L)).willReturn(null);

        mockMvc.perform(get("/historial/paciente/1").with(user("admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }

    // =========================
    // NUEVO
    // =========================
    @Test
    void testNuevo_Ok() throws Exception {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        given(pacienteService.buscarPorId(1L)).willReturn(paciente);

        mockMvc.perform(get("/historial/nuevo/1").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("historial/form"));
    }

    @Test
    void testNuevo_PacienteNull() throws Exception {

        given(pacienteService.buscarPorId(1L)).willReturn(null);

        mockMvc.perform(get("/historial/nuevo/1").with(user("admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }

    // =========================
    // GUARDAR (FIX REAL)
    // =========================
    @Test
    void testGuardar_Exito() throws Exception {

        PacienteEntity p = new PacienteEntity();
        p.setId(1L);

        HistorialEntity h = new HistorialEntity();
        h.setPaciente(p);

        given(historialService.guardar(any(HistorialEntity.class)))
                .willReturn(new HistorialEntity());

        mockMvc.perform(post("/historial/guardar")
                        .param("paciente.id", "1")
                        .flashAttr("historial", h)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/historial/paciente/1"));
    }

    @Test
    void testGuardar_Error() throws Exception {

        PacienteEntity p = new PacienteEntity();
        p.setId(1L);

        HistorialEntity h = new HistorialEntity();
        h.setPaciente(p);

        doThrow(new RuntimeException("Error"))
                .when(historialService).guardar(any());

        mockMvc.perform(post("/historial/guardar")
                        .flashAttr("historial", h)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("historial/form"))
                .andExpect(model().attributeExists("error"));
    }

    // =========================
    // PAGAR
    // =========================
    @Test
    void testPagar_Ok() throws Exception {

        PacienteEntity p = new PacienteEntity();
        p.setId(1L);

        HistorialEntity h = new HistorialEntity();
        h.setId(1L);
        h.setPaciente(p);

        given(historialService.buscarPorId(1L)).willReturn(h);

        mockMvc.perform(post("/historial/pagar/1")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/historial/paciente/1"));
    }

    @Test
    void testPagar_NoExiste() throws Exception {

        given(historialService.buscarPorId(1L)).willReturn(null);

        mockMvc.perform(post("/historial/pagar/1")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }

    // =========================
    // VER DETALLE
    // =========================
    @Test
    void testVerDetalle_Ok() throws Exception {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);
        paciente.setNombres("Juan Perez");

        HistorialEntity historial = new HistorialEntity();
        historial.setId(1L);
        historial.setPaciente(paciente);

        given(historialService.buscarPorId(1L)).willReturn(historial);

        mockMvc.perform(get("/historial/ver/1").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("historial/detalle"))
                .andExpect(model().attributeExists("historial"));
    }

    @Test
    void testVerDetalle_NoExiste() throws Exception {

        given(historialService.buscarPorId(1L)).willReturn(null);

        mockMvc.perform(get("/historial/ver/1").with(user("admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }
}