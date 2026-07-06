package com.digitaldentic.sistemaodontologico;



import com.digitaldentic.sistemaodontologico.controller.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDashboard_Ok() throws Exception {
        mockMvc.perform(get("/admin/dashboard").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"));
    }

    @Test
    void testLogout_InvalidatesSession() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/admin/logout")
                        .session(session)
                        .with(user("admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Verificamos que la sesión fue invalidada
        assert session.isInvalid();
    }
}