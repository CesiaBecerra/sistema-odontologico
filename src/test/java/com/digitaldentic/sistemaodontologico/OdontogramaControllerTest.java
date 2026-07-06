package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.OdontogramaEntity;
import com.digitaldentic.sistemaodontologico.Repository.HistorialRepository;
import com.digitaldentic.sistemaodontologico.Service.OdontogramaService;
import com.digitaldentic.sistemaodontologico.controller.OdontogramaController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = OdontogramaController.class,
        properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration"
)
class OdontogramaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OdontogramaService service;

    @MockBean
    private HistorialRepository historialRepo;

    @Test
    void testVerOdontograma_Ok() throws Exception {

        HistorialEntity historial = new HistorialEntity();
        historial.setOdontogramaAnotacion("imagen");

        given(historialRepo.findById(1L))
                .willReturn(Optional.of(historial));

        given(service.buscarPorHistorial(1L))
                .willReturn(Collections.emptyList());

        mockMvc.perform(get("/odontograma/historial/1")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("odontograma/premium"));
    }

    @Test
    void testVerOdontograma_HistorialNoExiste() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/odontograma/historial/1")
                        .with(user("admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testNuevo_Ok() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.of(new HistorialEntity()));

        mockMvc.perform(get("/odontograma/nuevo/1")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("odontograma/form"));
    }

    @Test
    void testNuevo_HistorialNoExiste() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/odontograma/nuevo/1")
                        .with(user("admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGuardar_Ok() throws Exception {

        HistorialEntity h = new HistorialEntity();
        h.setId(1L);

        OdontogramaEntity o = new OdontogramaEntity();
        o.setHistorial(h);

        mockMvc.perform(post("/odontograma/guardar")
                        .flashAttr("odontograma", o)
                        .param("historial.id", "1")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/odontograma/historial/1"));
    }

    @Test
    void testGuardar_SinHistorial() throws Exception {

        OdontogramaEntity o = new OdontogramaEntity();

        mockMvc.perform(post("/odontograma/guardar")
                        .flashAttr("odontograma", o)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGuardar_HistorialSinId() throws Exception {

        OdontogramaEntity o = new OdontogramaEntity();
        o.setHistorial(new HistorialEntity());

        mockMvc.perform(post("/odontograma/guardar")
                        .flashAttr("odontograma", o)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGuardarAjax_Ok() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.of(new HistorialEntity()));

        String json =
                "{\"historialId\":\"1\",\"pieza\":\"18\",\"superficie\":\"O\",\"estado\":\"CARIES\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void testGuardarAjax_HistorialNoExiste() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.empty());

        String json =
                "{\"historialId\":\"1\",\"pieza\":\"18\",\"superficie\":\"O\",\"estado\":\"CARIES\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ERROR"));
    }

    @Test
    void testGuardarAjax_IdInvalido() throws Exception {

        String json =
                "{\"historialId\":\"ABC\",\"pieza\":\"18\",\"superficie\":\"O\",\"estado\":\"CARIES\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGuardarAjax_ExceptionServicio() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.of(new HistorialEntity()));

        doThrow(new RuntimeException("error"))
                .when(service)
                .guardar(any(OdontogramaEntity.class));

        String json =
                "{\"historialId\":\"1\",\"pieza\":\"18\",\"superficie\":\"O\",\"estado\":\"CARIES\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGuardarAnotacion_Ok() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.of(new HistorialEntity()));

        String json =
                "{\"historialId\":\"1\",\"imagen\":\"data:image/png;base64,AAA\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar-anotacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        verify(historialRepo).save(any(HistorialEntity.class));
    }

    @Test
    void testGuardarAnotacion_HistorialIdNull() throws Exception {

        String json =
                "{\"imagen\":\"data:image/png;base64,AAA\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar-anotacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ERROR: datos incompletos"));
    }

    @Test
    void testGuardarAnotacion_ImagenNull() throws Exception {

        String json =
                "{\"historialId\":\"1\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar-anotacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ERROR: datos incompletos"));
    }

    @Test
    void testGuardarAnotacion_ImagenVacia() throws Exception {

        String json =
                "{\"historialId\":\"1\",\"imagen\":\"\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar-anotacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ERROR: datos incompletos"));
    }

    @Test
    void testGuardarAnotacion_HistorialNoExiste() throws Exception {

        given(historialRepo.findById(1L))
                .willReturn(Optional.empty());

        String json =
                "{\"historialId\":\"1\",\"imagen\":\"data:image/png;base64,AAA\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar-anotacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ERROR: historial no encontrado"));
    }

    @Test
    void testGuardarAnotacion_IdInvalido() throws Exception {

        String json =
                "{\"historialId\":\"ABC\",\"imagen\":\"data:image/png;base64,AAA\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar-anotacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGuardarAnotacion_ExceptionRepositorio() throws Exception {

        HistorialEntity historial = new HistorialEntity();

        given(historialRepo.findById(1L))
                .willReturn(Optional.of(historial));

        doThrow(new RuntimeException("error"))
                .when(historialRepo)
                .save(any(HistorialEntity.class));

        String json =
                "{\"historialId\":\"1\",\"imagen\":\"data:image/png;base64,AAA\"}";

        mockMvc.perform(post("/odontograma/ajax/guardar-anotacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }
}