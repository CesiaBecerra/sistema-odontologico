package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.RolEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Repository.UsuarioRepository;
import com.digitaldentic.sistemaodontologico.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock private UsuarioRepository repo;
    @Mock private PasswordEncoder encoder;
    @InjectMocks private UsuarioService service;

    // --- PRUEBAS DE GUARDAR ---
    @Test
    void testGuardar_NuevoUsuario_EncodaPassword() {
        UsuarioEntity u = new UsuarioEntity();
        u.setPassword("12345");
        when(encoder.encode("12345")).thenReturn("hashed");

        service.guardar(u);

        verify(repo).save(u);
        assertEquals("hashed", u.getPassword());
    }

    @Test
    void testGuardar_EditarUsuario_MantienePasswordSiEsNullOBlanco() {
        UsuarioEntity bd = new UsuarioEntity();
        bd.setId(1L);
        bd.setPassword("old_hash");
        UsuarioEntity update = new UsuarioEntity();
        update.setId(1L);
        update.setPassword(""); // Simula password vacío

        when(repo.findById(1L)).thenReturn(Optional.of(bd));

        service.guardar(update);

        assertEquals("old_hash", update.getPassword());
        verify(repo).save(update);
    }

    @Test
    void testGuardar_EditarUsuario_ActualizaPasswordSiEsNueva() {
        UsuarioEntity bd = new UsuarioEntity();
        bd.setId(1L);
        bd.setPassword("old_hash");
        UsuarioEntity update = new UsuarioEntity();
        update.setId(1L);
        update.setPassword("new_pass"); // Password nuevo

        when(repo.findById(1L)).thenReturn(Optional.of(bd));
        when(encoder.encode("new_pass")).thenReturn("new_hash");

        service.guardar(update);

        assertEquals("new_hash", update.getPassword()); // CUBRE LA LÍNEA ROJA (else)
        verify(repo).save(update);
    }

    // --- OTRAS PRUEBAS DE BÚSQUEDA Y LISTADO ---
    @Test
    void testBuscarPorId() {
        when(repo.findById(1L)).thenReturn(Optional.of(new UsuarioEntity()));
        assertNotNull(service.buscarPorId(1L)); // CUBRE LA LÍNEA ROJA (buscarPorId)
    }

    @Test
    void testListar_Y_Consultas() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        when(repo.findByEmail("test@test.com")).thenReturn(Optional.of(new UsuarioEntity()));
        when(repo.findByRol(RolEntity.DOCTOR)).thenReturn(Collections.emptyList());
        when(repo.findByRol(RolEntity.SECRETARIO)).thenReturn(Collections.emptyList());

        assertNotNull(service.listar());
        assertNotNull(service.buscarPorEmail("test@test.com"));
        assertNotNull(service.listarDoctores());
        assertNotNull(service.listarSecretarios());
    }

    @Test
    void testBuscar_Y_Eliminar() {
        when(repo.findById(1L)).thenReturn(Optional.of(new UsuarioEntity()));

        assertNotNull(service.buscar(1L));
        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }
}