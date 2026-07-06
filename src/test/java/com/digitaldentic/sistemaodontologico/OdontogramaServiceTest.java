package com.digitaldentic.sistemaodontologico;


import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.OdontogramaEntity;
import com.digitaldentic.sistemaodontologico.Repository.OdontogramaRepository;
import com.digitaldentic.sistemaodontologico.Service.OdontogramaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OdontogramaServiceTest {

    @Mock
    private OdontogramaRepository repo;

    @InjectMocks
    private OdontogramaService service;

    @Test
    void testBuscarPorHistorial() {
        Long id = 1L;
        when(repo.findByHistorialId(id)).thenReturn(new ArrayList<>());

        List<OdontogramaEntity> result = service.buscarPorHistorial(id);

        assertNotNull(result);
        verify(repo).findByHistorialId(id);
    }

    @Test
    void testListar() {
        when(repo.findAll()).thenReturn(new ArrayList<>());
        service.listar();
        verify(repo).findAll();
    }

    @Test
    void testEliminar() {
        Long id = 1L;
        service.eliminar(id);
        verify(repo).deleteById(id);
    }

    @Test
    void testGuardar_NuevoRegistro() {
        HistorialEntity h = new HistorialEntity();
        h.setId(1L);
        OdontogramaEntity o = new OdontogramaEntity();
        o.setHistorial(h);
        o.setPieza("18");
        o.setSuperficie("O");

        when(repo.findByHistorialIdAndPiezaAndSuperficie(1L, "18", "O")).thenReturn(null);
        when(repo.save(any(OdontogramaEntity.class))).thenReturn(o);

        OdontogramaEntity result = service.guardar(o);

        assertNotNull(result);
        verify(repo).save(o);
    }

    @Test
    void testGuardar_ActualizarExistente() {
        HistorialEntity h = new HistorialEntity();
        h.setId(1L);
        OdontogramaEntity existente = new OdontogramaEntity();
        existente.setEstado(null);

        OdontogramaEntity nuevo = new OdontogramaEntity();
        nuevo.setHistorial(h);
        nuevo.setPieza("18");
        nuevo.setSuperficie("O");
        nuevo.setEstado(null); // Estado nuevo

        when(repo.findByHistorialIdAndPiezaAndSuperficie(1L, "18", "O")).thenReturn(existente);
        when(repo.save(any(OdontogramaEntity.class))).thenReturn(existente);

        service.guardar(nuevo);

        verify(repo).save(existente);
    }
}