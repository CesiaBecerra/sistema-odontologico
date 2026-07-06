package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Service.UsuarioService;
import com.digitaldentic.sistemaodontologico.controller.UsuarioController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void testListarUsuarios_Ok() throws Exception {
        mockMvc.perform(get("/usuarios/lista").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios/lista"));
    }

    @Test
    void testNuevoUsuario_Ok() throws Exception {
        mockMvc.perform(get("/usuarios/nuevo").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios/form"));
    }

    @Test
    void testGuardarUsuario_Ok() throws Exception {
        mockMvc.perform(post("/usuarios/guardar")
                        .with(user("admin"))
                        .with(csrf()) // Necesario para peticiones POST
                        .param("nombre", "Juan"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/lista"));

        verify(usuarioService, times(1)).guardar(any(UsuarioEntity.class));
    }

    @Test
    void testEditarUsuario_Ok() throws Exception {
        when(usuarioService.buscar(1L)).thenReturn(new UsuarioEntity());

        mockMvc.perform(get("/usuarios/editar/1").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios/form"));
    }

    @Test
    void testEliminarUsuario_Ok() throws Exception {
        mockMvc.perform(get("/usuarios/eliminar/1").with(user("admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/lista"));

        verify(usuarioService, times(1)).eliminar(1L);
    }
}