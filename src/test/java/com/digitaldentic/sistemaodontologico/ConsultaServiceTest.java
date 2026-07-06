package com.digitaldentic.sistemaodontologico;



import com.digitaldentic.sistemaodontologico.Entity.ConsultaEntity;
import com.digitaldentic.sistemaodontologico.Repository.ConsultaRepository;
import com.digitaldentic.sistemaodontologico.Service.ConsultaService;
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
public class ConsultaServiceTest {

    @Mock
    private ConsultaRepository repo;

    @InjectMocks
    private ConsultaService service;

    @Test
    void testListar() {
        // Preparación
        when(repo.findAll()).thenReturn(new ArrayList<>());

        // Ejecución
        List<ConsultaEntity> result = service.listar();

        // Verificación
        assertNotNull(result);
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGuardar() {
        // Preparación
        ConsultaEntity consulta = new ConsultaEntity();
        when(repo.save(any(ConsultaEntity.class))).thenReturn(consulta);

        // Ejecución
        ConsultaEntity result = service.guardar(consulta);

        // Verificación
        assertNotNull(result);
        verify(repo, times(1)).save(consulta);
    }
}
