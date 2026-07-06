package com.digitaldentic.sistemaodontologico;



import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Entity.RolEntity;
import com.digitaldentic.sistemaodontologico.Repository.UsuarioRepository;
import com.digitaldentic.sistemaodontologico.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioDetailsServiceTest {

    @Test
    void testLoadUserByUsername_Ok() {

        UsuarioRepository repo = mock(UsuarioRepository.class);
        UsuarioDetailsService service = new UsuarioDetailsService(repo);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setEmail("test@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(RolEntity.ADMIN);
        usuario.setActivo(true);

        when(repo.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = service.loadUserByUsername("test@gmail.com");

        assertEquals("test@gmail.com", userDetails.getUsername());
        assertEquals("1234", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testLoadUserByUsername_NotFound() {

        UsuarioRepository repo = mock(UsuarioRepository.class);
        UsuarioDetailsService service = new UsuarioDetailsService(repo);

        when(repo.findByEmail("no@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> service.loadUserByUsername("no@mail.com")
        );
    }
}