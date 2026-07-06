package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Repository.HistorialRepository;
import com.digitaldentic.sistemaodontologico.Repository.PacienteRepository;
import com.digitaldentic.sistemaodontologico.Repository.UsuarioRepository;
import com.digitaldentic.sistemaodontologico.Service.HistorialService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HistorialServiceTest {

    @Mock
    private HistorialRepository repo;

    @Mock
    private PacienteRepository pacienteRepo;

    @Mock
    private UsuarioRepository usuarioRepo;

    @InjectMocks
    private HistorialService service;

    @BeforeEach
    void setupSecurity() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("doc@test.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }

    @Test
    void testListar() {

        when(repo.findAll()).thenReturn(Collections.emptyList());

        assertNotNull(service.listar());

        verify(repo).findAll();
    }

    @Test
    void testBuscarPorPaciente() {

        when(repo.findByPacienteIdOrderByFechaDesc(1L))
                .thenReturn(Collections.emptyList());

        assertNotNull(service.buscarPorPaciente(1L));
    }

    @Test
    void testBuscarPorId() {

        HistorialEntity historial = new HistorialEntity();

        when(repo.findById(1L))
                .thenReturn(Optional.of(historial));

        assertNotNull(service.buscarPorId(1L));
    }

    @Test
    void testBuscarPorId_NoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertNull(service.buscarPorId(1L));
    }

    @Test
    void testGuardar_Pagado() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);
        historial.setCostoTratamiento(100.0);
        historial.setMonto(100.0);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(usuarioRepo.findByEmail("doc@test.com"))
                .thenReturn(Optional.of(new UsuarioEntity()));

        when(repo.save(any(HistorialEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        HistorialEntity result = service.guardar(historial);

        assertEquals("PAGADO", result.getEstadoPago());
        assertEquals(0.0, result.getSaldo());
        assertNotNull(result.getFecha());
    }

    @Test
    void testGuardar_Pendiente() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);
        historial.setCostoTratamiento(100.0);
        historial.setMonto(0.0);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(usuarioRepo.findByEmail("doc@test.com"))
                .thenReturn(Optional.of(new UsuarioEntity()));

        when(repo.save(any(HistorialEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        HistorialEntity result = service.guardar(historial);

        assertEquals("PENDIENTE", result.getEstadoPago());
        assertEquals(100.0, result.getSaldo());
        assertNotNull(result.getFecha());
    }

    @Test
    void testGuardar_Parcial() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);
        historial.setCostoTratamiento(100.0);
        historial.setMonto(40.0);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(usuarioRepo.findByEmail("doc@test.com"))
                .thenReturn(Optional.of(new UsuarioEntity()));

        when(repo.save(any(HistorialEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        HistorialEntity result = service.guardar(historial);

        assertEquals("PARCIAL", result.getEstadoPago());
        assertEquals(60.0, result.getSaldo());
    }

    @Test
    void testGuardar_FechaExistente() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        LocalDate fecha = LocalDate.of(2025, 1, 1);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);
        historial.setFecha(fecha);
        historial.setCostoTratamiento(100.0);
        historial.setMonto(100.0);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(usuarioRepo.findByEmail("doc@test.com"))
                .thenReturn(Optional.of(new UsuarioEntity()));

        when(repo.save(any(HistorialEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        HistorialEntity result = service.guardar(historial);

        assertEquals(fecha, result.getFecha());
    }

    @Test
    void testGuardar_CostoNull_MontoNull() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);
        historial.setCostoTratamiento(null);
        historial.setMonto(null);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(usuarioRepo.findByEmail("doc@test.com"))
                .thenReturn(Optional.of(new UsuarioEntity()));

        when(repo.save(any(HistorialEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        HistorialEntity result = service.guardar(historial);

        assertEquals("PENDIENTE", result.getEstadoPago());
        assertEquals(0.0, result.getSaldo());
    }

    @Test
    void testGuardar_MontoMayorAlCosto() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);
        historial.setCostoTratamiento(100.0);
        historial.setMonto(150.0);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(usuarioRepo.findByEmail("doc@test.com"))
                .thenReturn(Optional.of(new UsuarioEntity()));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.guardar(historial));

        assertEquals(
                "El monto pagado no puede ser mayor al costo del tratamiento.",
                ex.getMessage());
    }

    @Test
    void testGuardar_PacienteNoEncontrado() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.guardar(historial));

        assertEquals("Paciente no encontrado", ex.getMessage());
    }

    @Test
    void testGuardar_DoctorNoEncontrado() {

        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);
        historial.setCostoTratamiento(100.0);
        historial.setMonto(50.0);

        when(pacienteRepo.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(usuarioRepo.findByEmail("doc@test.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.guardar(historial));

        assertEquals("Doctor no encontrado", ex.getMessage());
    }

    @Test
    void testMarcarComoPagado() {

        HistorialEntity historial = new HistorialEntity();
        historial.setSaldo(50.0);

        when(repo.findById(1L))
                .thenReturn(Optional.of(historial));

        service.marcarComoPagado(1L);

        assertEquals("PAGADO", historial.getEstadoPago());
        assertEquals(0.0, historial.getSaldo());

        verify(repo).save(historial);
    }

    @Test
    void testMarcarComoPagado_HistorialNoEncontrado() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.marcarComoPagado(1L));

        assertEquals("Historial no encontrado", ex.getMessage());
    }
}