package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.Entity.CajaEntity;
import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.PagoEntity;
import com.digitaldentic.sistemaodontologico.Repository.CajaRepository;
import com.digitaldentic.sistemaodontologico.Repository.HistorialRepository;
import com.digitaldentic.sistemaodontologico.Repository.PagoRepository;
import com.digitaldentic.sistemaodontologico.Service.PagoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository repo;

    @Mock
    private HistorialRepository historialRepo;

    @Mock
    private CajaRepository cajaRepo;

    @InjectMocks
    private PagoService service;

    @Test
    void testGuardar_CoberturaCompleta() {

        // Historial no encontrado
        PagoEntity p1 = new PagoEntity();
        HistorialEntity h1 = new HistorialEntity();
        h1.setId(99L);
        p1.setHistorial(h1);

        when(historialRepo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.guardar(p1));

        // Monto <= 0
        PagoEntity p2 = new PagoEntity();
        p2.setMonto(0.0);

        HistorialEntity h2 = new HistorialEntity();
        h2.setId(2L);
        h2.setCostoTratamiento(100.0);

        p2.setHistorial(h2);

        when(historialRepo.findById(2L))
                .thenReturn(Optional.of(h2));

        assertThrows(RuntimeException.class,
                () -> service.guardar(p2));

        // Pago mayor a deuda
        PagoEntity p3 = new PagoEntity();
        p3.setMonto(500.0);

        HistorialEntity h3 = new HistorialEntity();
        h3.setId(3L);
        h3.setCostoTratamiento(100.0);

        p3.setHistorial(h3);

        when(historialRepo.findById(3L))
                .thenReturn(Optional.of(h3));

        when(repo.totalPagadoPorHistorial(3L))
                .thenReturn(0.0);

        assertThrows(RuntimeException.class,
                () -> service.guardar(p3));

        // totalPagado = null y ultimoId = null
        PagoEntity p4 = new PagoEntity();
        p4.setMonto(10.0);

        HistorialEntity h4 = new HistorialEntity();
        h4.setId(4L);
        h4.setCostoTratamiento(100.0);

        p4.setHistorial(h4);

        when(historialRepo.findById(4L))
                .thenReturn(Optional.of(h4));

        when(repo.totalPagadoPorHistorial(4L))
                .thenReturn(null);

        when(repo.ultimoId())
                .thenReturn(null);

        when(repo.save(any(PagoEntity.class)))
                .thenReturn(p4);

        assertNotNull(service.guardar(p4));

        // totalPagado != null y ultimoId != null
        PagoEntity p5 = new PagoEntity();
        p5.setMonto(10.0);

        HistorialEntity h5 = new HistorialEntity();
        h5.setId(5L);
        h5.setCostoTratamiento(100.0);

        p5.setHistorial(h5);

        when(historialRepo.findById(5L))
                .thenReturn(Optional.of(h5));

        when(repo.totalPagadoPorHistorial(5L))
                .thenReturn(20.0);

        when(repo.ultimoId())
                .thenReturn(10L);

        when(repo.save(any(PagoEntity.class)))
                .thenReturn(p5);

        assertNotNull(service.guardar(p5));
    }

    @Test
    void testGuardar_VerificarDatosGenerados() {

        HistorialEntity historial = new HistorialEntity();
        historial.setId(10L);
        historial.setCostoTratamiento(200.0);

        PagoEntity pago = new PagoEntity();
        pago.setMonto(50.0);
        pago.setHistorial(historial);

        when(historialRepo.findById(10L))
                .thenReturn(Optional.of(historial));

        when(repo.totalPagadoPorHistorial(10L))
                .thenReturn(20.0);

        when(repo.ultimoId())
                .thenReturn(5L);

        when(repo.save(any(PagoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PagoEntity resultado = service.guardar(pago);

        assertEquals("COMP-2026-00006",
                resultado.getNumeroComprobante());

        assertEquals(LocalDate.now(),
                resultado.getFechaPago());

        assertEquals(70.0,
                historial.getMonto());

        verify(historialRepo).save(historial);
        verify(repo).save(pago);
    }

    @Test
    void testGuardar_MontoNull_Y_TernarioMonto() {

        // p.getMonto() == null
        PagoEntity pagoNull = new PagoEntity();

        HistorialEntity historial1 = new HistorialEntity();
        historial1.setId(20L);
        historial1.setCostoTratamiento(100.0);

        pagoNull.setHistorial(historial1);
        pagoNull.setMonto(null);

        when(historialRepo.findById(20L))
                .thenReturn(Optional.of(historial1));

        when(repo.totalPagadoPorHistorial(20L))
                .thenReturn(0.0);

        assertThrows(RuntimeException.class,
                () -> service.guardar(pagoNull));

        // totalPagado != null
        PagoEntity pago = new PagoEntity();
        pago.setMonto(15.0);

        HistorialEntity historial2 = new HistorialEntity();
        historial2.setId(21L);
        historial2.setCostoTratamiento(100.0);

        pago.setHistorial(historial2);

        when(historialRepo.findById(21L))
                .thenReturn(Optional.of(historial2));

        when(repo.totalPagadoPorHistorial(21L))
                .thenReturn(30.0);

        when(repo.ultimoId())
                .thenReturn(1L);

        when(repo.save(any(PagoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.guardar(pago);

        assertEquals(45.0, historial2.getMonto());

        verify(historialRepo).save(historial2);
    }

    @Test
    void testConsultas_CoberturaTotal() {

        PagoEntity p1 = new PagoEntity();
        p1.setMonto(null);

        PagoEntity p2 = new PagoEntity();
        p2.setMonto(50.0);

        when(repo.findAll())
                .thenReturn(Arrays.asList(p1, p2));

        when(repo.findByFechaPagoBetween(any(), any()))
                .thenReturn(Arrays.asList(p1, p2));

        when(repo.findById(1L))
                .thenReturn(Optional.of(p1));

        when(repo.findByFechaPago(any()))
                .thenReturn(Collections.singletonList(p1));

        assertEquals(50.0, service.total());

        assertEquals(50.0,
                service.totalPorRango(LocalDate.now(), LocalDate.now()));

        assertNotNull(service.listar());
        assertNotNull(service.buscarPorId(1L));
        assertNotNull(service.pagosHoy());
        assertNotNull(service.totalHoy());

        service.cerrarCaja();

        verify(cajaRepo, atLeastOnce())
                .save(any(CajaEntity.class));
    }

    @Test
    void testMetodosAdicionales_Cobertura() {

        PagoEntity p = new PagoEntity();
        p.setMetodoPago("EFECTIVO");

        PagoEntity pNull = new PagoEntity();
        pNull.setMetodoPago(null);

        when(repo.findAll())
                .thenReturn(Arrays.asList(p, pNull));

        Map<String, Long> result = service.conteoMetodo();

        assertEquals(1L, result.get("EFECTIVO"));
        assertEquals(1L, result.get("SIN MÉTODO"));

        HistorialEntity h1 = new HistorialEntity();
        h1.setCostoTratamiento(100.0);

        when(historialRepo.findById(1L))
                .thenReturn(Optional.of(h1));

        when(repo.totalPagadoPorHistorial(1L))
                .thenReturn(null);

        assertEquals(100.0,
                service.deudaHistorial(1L));

        when(repo.totalPagadoPorHistorial(1L))
                .thenReturn(20.0);

        assertEquals(80.0,
                service.deudaHistorial(1L));
    }

    @Test
    void testMetodosFaltantes_Cobertura100() {

        PagoEntity pago = new PagoEntity();

        when(repo.findByHistorialId(1L))
                .thenReturn(Collections.singletonList(pago));

        assertEquals(1,
                service.buscarPorHistorial(1L).size());

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertNull(service.findById(99L));

        when(repo.findById(100L))
                .thenReturn(Optional.empty());

        assertNull(service.buscarPorId(100L));

        when(repo.findByFechaPagoBetween(any(), any()))
                .thenReturn(Collections.singletonList(pago));

        assertEquals(1,
                service.pagosPorRango(LocalDate.now(), LocalDate.now()).size());

        PagoEntity pagoHoy = new PagoEntity();
        pagoHoy.setMonto(25.0);

        when(repo.findByFechaPago(any()))
                .thenReturn(Collections.singletonList(pagoHoy));

        assertEquals(25.0, service.totalHoy());

        when(historialRepo.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.deudaHistorial(999L));
    }
}