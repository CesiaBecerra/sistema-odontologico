package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.*;
import com.digitaldentic.sistemaodontologico.Service.CitaService;
import com.digitaldentic.sistemaodontologico.Service.UsuarioService;
import com.digitaldentic.sistemaodontologico.controller.CitaController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaController.class)
public class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaService citaService;

    @MockBean
    private UsuarioService usuarioService;

    // =========================
    // FORMULARIO
    // =========================
    @Test
    void testFormularioReserva_Ok() throws Exception {
        given(usuarioService.listarDoctores()).willReturn(List.of());

        mockMvc.perform(get("/cita/reservar").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cita"))
                .andExpect(model().attributeExists("doctores"));
    }

    // =========================
    // HORAS DISPONIBLES
    // =========================
    @Test
    void testHorasDisponibles_Ok() throws Exception {

        UsuarioEntity doctor = new UsuarioEntity();
        doctor.setId(1L);

        CitaEntity cita = new CitaEntity();
        cita.setFechaHora(LocalDateTime.of(2026, 7, 5, 10, 0));

        given(usuarioService.buscarPorId(eq(1L))).willReturn(doctor);

        given(citaService.citasDelDia(
                any(UsuarioEntity.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(List.of(cita));

        mockMvc.perform(get("/cita/horas-disponibles")
                        .param("doctorId", "1")
                        .param("fecha", "2026-07-05")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // =========================
    // GUARDAR OK
    // =========================
    @Test
    void testGuardarCita_Ok() throws Exception {

        UsuarioEntity doctor = new UsuarioEntity();
        doctor.setId(1L);

        given(usuarioService.buscarPorId(anyLong())).willReturn(doctor);

        given(citaService.existeCita(
                any(UsuarioEntity.class),
                any(LocalDateTime.class)
        )).willReturn(false);

        mockMvc.perform(post("/cita/guardar")
                        .param("doctorId", "1")
                        .param("fechaHora", "2026-07-05T10:00:00")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cita/confirmacion"));
    }

    // =========================
    // CONFLICTO
    // =========================
    @Test
    void testGuardarCita_Conflicto_Ok() throws Exception {

        UsuarioEntity doctor = new UsuarioEntity();
        doctor.setId(1L);

        given(usuarioService.buscarPorId(anyLong())).willReturn(doctor);

        given(citaService.existeCita(
                any(UsuarioEntity.class),
                any(LocalDateTime.class)
        )).willReturn(true);

        mockMvc.perform(post("/cita/guardar")
                        .param("doctorId", "1")
                        .param("fechaHora", "2026-07-05T10:00:00")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cita/reservar"));
    }

    // =========================
    // CONFIRMACION
    // =========================
    @Test
    void testConfirmacion_Ok() throws Exception {
        mockMvc.perform(get("/cita/confirmacion").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("cita/confirmacion"));
    }

    // =========================
    // LISTA
    // =========================
    @Test
    void testListarTodas_Ok() throws Exception {
        given(citaService.listarTodas()).willReturn(List.of());

        mockMvc.perform(get("/cita/lista").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("citas"));
    }

    // =========================
    // MIS CITAS
    // =========================
    @Test
    void testListarMisCitas_Ok() throws Exception {

        UsuarioEntity doctor = new UsuarioEntity();
        doctor.setId(1L);

        given(usuarioService.buscarPorEmail(anyString())).willReturn(doctor);

        given(citaService.listarPorDoctor(any(UsuarioEntity.class)))
                .willReturn(List.of());

        mockMvc.perform(get("/cita/mis-citas")
                        .principal((Principal) () -> "test@test.com")
                        .with(user("doctor")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("citas"));
    }

    // =========================
    // ACTUALIZAR ESTADO ADMIN
    // =========================
    @Test
    void testActualizarEstado_AdminCancelar_Ok() throws Exception {

        UsuarioEntity user = new UsuarioEntity();
        user.setRol(RolEntity.ADMIN);

        CitaEntity cita = new CitaEntity();

        given(usuarioService.buscarPorEmail(anyString())).willReturn(user);
        given(citaService.buscarPorId(anyLong())).willReturn(cita);

        mockMvc.perform(post("/cita/actualizar-estado")
                        .param("id", "1")
                        .param("estado", "CANCELADA")
                        .principal((Principal) () -> "admin@test.com")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    // =========================
    // DOCTOR NO AUTORIZADO
    // =========================
    @Test
    void testActualizarEstado_DoctorNoAutorizado_Redirect() throws Exception {

        UsuarioEntity doc = new UsuarioEntity();
        doc.setId(1L);
        doc.setRol(RolEntity.DOCTOR);

        UsuarioEntity otroDoctor = new UsuarioEntity();
        otroDoctor.setId(2L);

        CitaEntity cita = new CitaEntity();
        cita.setDoctor(otroDoctor);

        given(usuarioService.buscarPorEmail(anyString())).willReturn(doc);
        given(citaService.buscarPorId(anyLong())).willReturn(cita);

        mockMvc.perform(post("/cita/actualizar-estado")
                        .param("id", "1")
                        .param("estado", "CANCELADA")
                        .principal((Principal) () -> "doc@test.com")
                        .with(user("doctor"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}