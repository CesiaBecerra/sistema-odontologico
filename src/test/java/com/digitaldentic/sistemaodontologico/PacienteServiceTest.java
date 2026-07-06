package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;
import com.digitaldentic.sistemaodontologico.Repository.PacienteRepository;
import com.digitaldentic.sistemaodontologico.Service.PacienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @Mock
    private PacienteRepository repo;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void testGuardarPaciente_Exitoso() {
        PacienteEntity p = new PacienteEntity();
        p.setId(1L);
        p.setDni("12345678");

        given(repo.findByDni("12345678")).willReturn(Optional.empty());
        given(repo.save(any(PacienteEntity.class))).willReturn(p);

        PacienteEntity resultado = pacienteService.guardar(p);
        assertNotNull(resultado);
        assertEquals("12345678", resultado.getDni());
    }

    @Test
    void testGuardar_DniDuplicado_LanzaExcepcion() {
        PacienteEntity pExistente = new PacienteEntity(); pExistente.setId(1L); pExistente.setDni("123");
        PacienteEntity pNuevo = new PacienteEntity(); pNuevo.setId(2L); pNuevo.setDni("123");

        given(repo.findByDni("123")).willReturn(Optional.of(pExistente));

        assertThrows(RuntimeException.class, () -> pacienteService.guardar(pNuevo));
    }

    @Test
    void testConsultas_Basicas() {
        // Mocking de todos los métodos de consulta
        given(repo.findAll()).willReturn(Collections.emptyList());
        given(repo.listarPacientesPorDoctor(1L)).willReturn(Collections.emptyList());
        given(repo.findByNombresContainingIgnoreCase("Juan")).willReturn(Collections.emptyList());
        given(repo.findByDni("123")).willReturn(Optional.empty());
        given(repo.findById(1L)).willReturn(Optional.empty());

        assertNotNull(pacienteService.listar());
        assertNotNull(pacienteService.listarPacientesPorDoctor(1L));
        assertNotNull(pacienteService.buscarPorNombre("Juan"));
        assertNull(pacienteService.buscarPorDni("123"));
        assertNull(pacienteService.buscarPorId(1L));
    }

    @Test
    void testEliminar_Exitoso() {
        doNothing().when(repo).deleteById(1L);
        pacienteService.eliminar(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}