package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.controller.LoginRedirectController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginRedirectController.class)
public class LoginRedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRedirectAdmin() throws Exception {
        mockMvc.perform(get("/redirect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard")); // Sin el prefijo "redirect:"
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void testRedirectDoctor() throws Exception {
        mockMvc.perform(get("/redirect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctor/dashboard"));
    }

    @Test
    @WithMockUser(roles = "SECRETARIO")
    void testRedirectSecretario() throws Exception {
        mockMvc.perform(get("/redirect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/secretario/dashboard"));
    }

    @Test
    @WithMockUser(roles = "USUARIO_NORMAL")
    void testRedirectLogin() throws Exception {
        mockMvc.perform(get("/redirect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}