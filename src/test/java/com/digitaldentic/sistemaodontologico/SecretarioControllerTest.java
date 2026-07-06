package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.controller.SecretarioController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser; // Asegúrate de este import
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecretarioController.class)
public class SecretarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "SECRETARIO") // <--- ESTO ES LO QUE FALTA
    void testDashboard() throws Exception {
        mockMvc.perform(get("/secretario/dashboard"))
                .andExpect(status().isOk());
    }
}