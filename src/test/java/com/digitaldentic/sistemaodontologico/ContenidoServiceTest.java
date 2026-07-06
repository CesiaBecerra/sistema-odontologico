package com.digitaldentic.sistemaodontologico;



import com.digitaldentic.sistemaodontologico.Entity.ContenidoEntity;
import com.digitaldentic.sistemaodontologico.Repository.ContenidoRepository;
import com.digitaldentic.sistemaodontologico.Service.ContenidoService;
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
public class ContenidoServiceTest {

    @Mock
    private ContenidoRepository repo;

    @InjectMocks
    private ContenidoService service;

    @Test
    void testListar() {
        when(repo.findAll()).thenReturn(new ArrayList<>());

        List<ContenidoEntity> result = service.listar();

        assertNotNull(result);
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGuardar() {
        ContenidoEntity contenido = new ContenidoEntity();
        when(repo.save(any(ContenidoEntity.class))).thenReturn(contenido);

        ContenidoEntity result = service.guardar(contenido);

        assertNotNull(result);
        verify(repo, times(1)).save(contenido);
    }
}