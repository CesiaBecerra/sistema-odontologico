package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.PagoEntity;
import com.digitaldentic.sistemaodontologico.Service.HistorialService;
import com.digitaldentic.sistemaodontologico.Service.PagoService;
import com.digitaldentic.sistemaodontologico.controller.PagoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private HistorialService historialService;

    @Test
    void testVerPagos_DebeRetornarVistaLista() throws Exception {
        Long id = 1L;
        HistorialEntity h = new HistorialEntity();
        h.setId(id);
        given(historialService.buscarPorId(id)).willReturn(h);
        given(pagoService.buscarPorHistorial(id)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/pago/historial/{id}", id).with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("pago/lista"));
    }

    @Test
    void testNuevoPago_DebeRetornarVistaForm() throws Exception {
        Long id = 1L;
        HistorialEntity h = new HistorialEntity();
        h.setId(id);
        given(historialService.buscarPorId(id)).willReturn(h);
        given(pagoService.deudaHistorial(id)).willReturn(100.0);

        mockMvc.perform(get("/pago/nuevo/{historialId}", id).with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("pago/form"));
    }

    @Test
    void testGuardarPago_DebeRedireccionar() throws Exception {
        mockMvc.perform(post("/pago/guardar").param("historialId", "1").with(user("admin")).with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testLista_DebeRetornarVistaLista() throws Exception {
        given(pagoService.listar()).willReturn(Collections.emptyList());
        given(pagoService.total()).willReturn(0.0);
        given(pagoService.conteoMetodo()).willReturn(new HashMap<>());

        mockMvc.perform(get("/pago/lista").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("pago/lista"));
    }

    @Test
    void testExportarExcel_DebeRetornarOk() throws Exception {
        mockMvc.perform(get("/pago/exportar-excel").with(user("admin")))
                .andExpect(status().isOk());
    }

    @Test
    void testGenerarPDF_DebeRetornarOk() throws Exception {
        // CORREGIDO: La ruta debe coincidir con @GetMapping("/comprobante/pdf/{id}")
        PagoEntity p = new PagoEntity();
        p.setNumeroComprobante("TEST");
        given(pagoService.buscarPorId(1L)).willReturn(p);

        mockMvc.perform(get("/pago/comprobante/pdf/{id}", 1L).with(user("admin")))
                .andExpect(status().isOk());
    }

    @Test
    void testFiltrarPorFechas_DebeRetornarVistaLista() throws Exception {
        mockMvc.perform(get("/pago/filtrar").param("inicio", "2026-01-01").param("fin", "2026-01-07").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("pago/lista"));
    }

    @Test
    void testTratamientosPendientes_DebeRetornarVista() throws Exception {
        mockMvc.perform(get("/pago/pendientes").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("pago/pendientes"));
    }

    @Test
    void testVerComprobante_DebeRetornarVista() throws Exception {
        PagoEntity p = new PagoEntity();
        p.setId(1L);
        p.setNumeroComprobante("TEST");
        given(pagoService.buscarPorId(1L)).willReturn(p);

        mockMvc.perform(get("/pago/comprobante/{id}", 1L).with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("pago/comprobante"));
    }

    @Test
    void testCerrarCaja_DebeRedireccionar() throws Exception {
        // CORREGIDO: Post en lugar de Get para cerrar caja
        mockMvc.perform(post("/pago/cerrar-caja").with(user("admin")).with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}