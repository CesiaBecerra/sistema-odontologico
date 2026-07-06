package com.digitaldentic.sistemaodontologico;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAccesoDenegadoSinLogin() throws Exception {
        // Verifica que la ruta protegida redirija al login si no estás autenticado
        mockMvc.perform(get("/cita/lista"))
                .andExpect(status().is3xxRedirection());
    }
}
