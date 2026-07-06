package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.CitaEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Entity.EstadocitaEntity;
import com.digitaldentic.sistemaodontologico.Repository.CitaRepository;

import com.digitaldentic.sistemaodontologico.Service.CitaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository repo;

    @InjectMocks
    private CitaService citaService;

    @Test
    void listarTodas_ok() {
        when(repo.findAll()).thenReturn(Arrays.asList(new CitaEntity()));

        List<CitaEntity> result = citaService.listarTodas();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void listarPorDoctor_ok() {
        UsuarioEntity doctor = new UsuarioEntity();

        when(repo.findByDoctor(doctor))
                .thenReturn(Arrays.asList(new CitaEntity()));

        List<CitaEntity> result = citaService.listarPorDoctor(doctor);

        assertEquals(1, result.size());
    }

    @Test
    void existeCita_ok() {
        UsuarioEntity doctor = new UsuarioEntity();
        LocalDateTime fecha = LocalDateTime.now();

        when(repo.existsByDoctorAndFechaHora(doctor, fecha))
                .thenReturn(true);

        boolean result = citaService.existeCita(doctor, fecha);

        assertTrue(result);
    }

    @Test
    void guardar_ok() {
        CitaEntity cita = new CitaEntity();

        when(repo.save(cita)).thenReturn(cita);

        CitaEntity result = citaService.guardar(cita);

        assertNotNull(result);
        verify(repo).save(cita);
    }

    @Test
    void citasDelDia_ok() {
        UsuarioEntity doctor = new UsuarioEntity();

        when(repo.findByDoctorAndFechaHoraBetween(any(), any(), any()))
                .thenReturn(Arrays.asList(new CitaEntity()));

        List<CitaEntity> result = citaService.citasDelDia(
                doctor,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorId_ok() {
        CitaEntity cita = new CitaEntity();

        when(repo.findById(1L))
                .thenReturn(Optional.of(cita));

        CitaEntity result = citaService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    void actualizarEstado_ok() {
        CitaEntity cita = new CitaEntity();
        UsuarioEntity doctor = new UsuarioEntity();
        cita.setDoctor(doctor);

        when(repo.findById(1L))
                .thenReturn(Optional.of(cita));

        citaService.actualizarEstado(1L, EstadocitaEntity.CANCELADA);

        verify(repo).save(cita);
    }
}