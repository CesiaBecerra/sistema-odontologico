package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.controller.DoctorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDashboard_Ok() throws Exception {
        // Simulamos el acceso como un usuario autenticado para evitar errores 403
        mockMvc.perform(get("/doctor/dashboard").with(user("doctor")))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor/dashboard"));
    }
}