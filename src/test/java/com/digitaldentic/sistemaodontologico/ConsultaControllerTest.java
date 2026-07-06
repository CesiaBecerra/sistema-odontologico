package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.ConsultaEntity;
import com.digitaldentic.sistemaodontologico.Service.ConsultaService;
import com.digitaldentic.sistemaodontologico.controller.ConsultaController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

// Imports para seguridad
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 1. Excluimos ThymeleafAutoConfiguration para evitar el error de resolución
@WebMvcTest(controllers = ConsultaController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService consultaService;

    // 2. Configuramos un ViewResolver "nulo" para que no intente buscar archivos .html
    @TestConfiguration
    static class TestConfig {
        @Bean
        public ViewResolver viewResolver() {
            return new InternalResourceViewResolver();
        }
    }

    @Test
    void testFlujosConsulta() throws Exception {
        // GET /consultas/nueva
        mockMvc.perform(get("/consultas/nueva"))
                .andExpect(status().isOk());

        // POST /consultas/guardar
        mockMvc.perform(post("/consultas/guardar")
                        .with(csrf())
                        .flashAttr("consulta", new ConsultaEntity()))
                .andExpect(status().is3xxRedirection());

        // GET /consultas/enviada
        mockMvc.perform(get("/consultas/enviada"))
                .andExpect(status().isOk());
    }
}