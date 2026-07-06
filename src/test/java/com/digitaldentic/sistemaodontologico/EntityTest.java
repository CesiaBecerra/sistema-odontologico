package com.digitaldentic.sistemaodontologico;



import com.digitaldentic.sistemaodontologico.Entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    // ===================== CITA =====================
    @Test
    void testCitaEntity() {
        CitaEntity c = new CitaEntity();

        c.setId(1L);
        c.setFechaHora(LocalDateTime.now());
        c.setNombrePaciente("Juan");
        c.setTelefono("999");
        c.setMotivoConsulta("Dolor");
        c.setEstado(EstadocitaEntity.RESERVADA);

        assertEquals(1L, c.getId());
        assertEquals("Juan", c.getNombrePaciente());
        assertEquals("999", c.getTelefono());
        assertEquals("Dolor", c.getMotivoConsulta());
        assertEquals(EstadocitaEntity.RESERVADA, c.getEstado());
    }

    // ===================== CONSULTA =====================
    @Test
    void testConsultaEntity() {
        ConsultaEntity c = new ConsultaEntity();

        c.setNombre("Ana");
        c.setTelefono("123");
        c.setMensaje("Consulta");
        c.setDescripcion("Dolor muela");
        c.setAtendido(true);

        assertEquals("Ana", c.getNombre());
        assertTrue(c.isAtendido());
    }

    // ===================== PAGO =====================
    @Test
    void testSettersAndGetters() {

        PagoEntity pago = new PagoEntity();

        PacienteEntity paciente = new PacienteEntity();
        HistorialEntity historial = new HistorialEntity();

        pago.setId(1L);
        pago.setMonto(150.0);
        pago.setMetodoPago("EFECTIVO");
        pago.setFechaPago(LocalDate.of(2026, 1, 1));
        pago.setNumeroComprobante("COMP-001");
        pago.setPaciente(paciente);
        pago.setHistorial(historial);

        assertEquals(1L, pago.getId());
        assertEquals(150.0, pago.getMonto());
        assertEquals("EFECTIVO", pago.getMetodoPago());
        assertEquals(LocalDate.of(2026, 1, 1), pago.getFechaPago());
        assertEquals("COMP-001", pago.getNumeroComprobante());
        assertEquals(paciente, pago.getPaciente());
        assertEquals(historial, pago.getHistorial());
    }

    @Test
    void testPrePersist_DefaultValues() {

        PagoEntity pago = new PagoEntity();

        pago.prePersist();

        assertNotNull(pago.getFechaPago());   // debe asignar fecha actual
        assertEquals(0.0, pago.getMonto());    // monto por defecto
    }

    @Test
    void testPrePersist_HistorialAsignaPaciente() {

        PacienteEntity paciente = new PacienteEntity();

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente);

        PagoEntity pago = new PagoEntity();
        pago.setHistorial(historial);
        pago.setPaciente(null);

        pago.prePersist();

        assertEquals(paciente, pago.getPaciente());
    }

    @Test
    void testPrePersist_NoSobrescribePacienteExistente() {

        PacienteEntity paciente1 = new PacienteEntity();
        PacienteEntity paciente2 = new PacienteEntity();

        HistorialEntity historial = new HistorialEntity();
        historial.setPaciente(paciente1);

        PagoEntity pago = new PagoEntity();
        pago.setHistorial(historial);
        pago.setPaciente(paciente2);

        pago.prePersist();

        // debe respetar el paciente ya asignado
        assertEquals(paciente2, pago.getPaciente());
    }

    // ===================== CONTENIDO =====================
    @Test
    void testContenidoEntity_SettersAndGetters() {

        ContenidoEntity c = new ContenidoEntity();

        UsuarioEntity usuario = new UsuarioEntity();

        LocalDate fecha = LocalDate.of(2026, 1, 1);

        c.setId(1L);
        c.setTitulo("Ortodoncia básica");
        c.setDescripcion("Contenido educativo sobre ortodoncia");
        c.setUrlVideo("https://video.com/test");
        c.setImagen("imagen.png");
        c.setActivo(true);
        c.setFechaPublicacion(fecha);
        c.setPublicadoPor(usuario);

        assertEquals(1L, c.getId());
        assertEquals("Ortodoncia básica", c.getTitulo());
        assertEquals("Contenido educativo sobre ortodoncia", c.getDescripcion());
        assertEquals("https://video.com/test", c.getUrlVideo());
        assertEquals("imagen.png", c.getImagen());
        assertTrue(c.isActivo());
        assertEquals(fecha, c.getFechaPublicacion());
        assertEquals(usuario, c.getPublicadoPor());
    }

    @Test
    void testContenidoEntity_DefaultValues() {

        ContenidoEntity c = new ContenidoEntity();

        assertNull(c.getId());
        assertNull(c.getTitulo());
        assertNull(c.getDescripcion());
        assertNull(c.getUrlVideo());
        assertNull(c.getImagen());
        assertFalse(c.isActivo()); // boolean default = false
        assertNull(c.getFechaPublicacion());
        assertNull(c.getPublicadoPor());
    }
    // ===================== ODONTOGRAMA =====================
    @Test
    void testOdontogramaEntity() {
        OdontogramaEntity o = new OdontogramaEntity();

        o.setPieza("Molar");
        o.setSuperficie("Superior");
        o.setTratamiento("Limpieza");

        assertEquals("Molar", o.getPieza());
        assertEquals("Superior", o.getSuperficie());
        assertEquals("Limpieza", o.getTratamiento());
    }

    // ===================== HISTORIAL =====================

    @Test
    void testPacienteEntity() {

        PacienteEntity p = new PacienteEntity();

        p.setId(1L);
        p.setNombres("Juan");
        p.setApellidos("Perez");
        p.setDni("12345678");
        p.setTelefono("999999999");
        p.setEmail("test@mail.com");
        p.setDireccion("Lima");
        p.setNacimiento(LocalDate.of(2000, 1, 1));
        p.setSexo(SexoEntity.MASCULINO);

        assertEquals(1L, p.getId());
        assertEquals("Juan", p.getNombres());
        assertEquals("Perez", p.getApellidos());
        assertEquals("12345678", p.getDni());
        assertEquals("999999999", p.getTelefono());
        assertEquals("test@mail.com", p.getEmail());
        assertEquals("Lima", p.getDireccion());
        assertEquals(LocalDate.of(2000, 1, 1), p.getNacimiento());
        assertEquals(SexoEntity.MASCULINO, p.getSexo());
    }

    @Test
    void testPacienteEntityNullValues() {

        PacienteEntity p = new PacienteEntity();

        assertNull(p.getId());
        assertNull(p.getNombres());
        assertNull(p.getApellidos());
        assertNull(p.getDni());
        assertNull(p.getTelefono());
        assertNull(p.getEmail());
        assertNull(p.getDireccion());
        assertNull(p.getNacimiento());
        assertNull(p.getSexo());
    }

    @Test
    void testCalcularSaldo_Pendiente() {
        HistorialEntity h = new HistorialEntity();
        h.setCostoTratamiento(100.0);
        h.setMonto(0.0);
        h.prePersist();

        assertEquals(100.0, h.getSaldo());
        assertEquals("PENDIENTE", h.getEstadoPago());
    }

    @Test
    void testGettersYSettersCompletos() {
        HistorialEntity h = new HistorialEntity();
        h.setId(1L);
        h.setMotivoConsulta("Dolor");
        h.setSintomas("Inflamación");
        h.setDiagnostico("Caries");
        h.setPlanTratamiento("Limpieza");
        h.setObservaciones("Ninguna");
        h.setDiabetes(true);
        h.setHipertension(false);
        h.setAlergias(true);
        h.setEmbarazo(false);
        h.setEpilepsia(false);
        h.setPrcardiacos(false);
        h.setHepatitis(false);
        h.setEnfrenales(false);
        h.setTbc(false);
        h.setOdontogramaAnotacion("Nota");

        // AQUÍ ESTÁ EL CAMBIO: Asignamos objetos que tengan ID
        PacienteEntity p = new PacienteEntity();
        p.setId(1L);
        h.setPaciente(p);

        UsuarioEntity u = new UsuarioEntity();
        u.setId(1L);
        h.setDoctor(u);

        assertEquals(1L, h.getId());
        assertEquals("Dolor", h.getMotivoConsulta());
        assertEquals("Inflamación", h.getSintomas());
        assertTrue(h.isDiabetes());
        assertTrue(h.isAlergias());
        assertEquals("Nota", h.getOdontogramaAnotacion());

        // Verificamos que no sean null
        assertNotNull(h.getPaciente());
        assertNotNull(h.getDoctor());
        assertEquals(1L, h.getPaciente().getId());
    }
}
