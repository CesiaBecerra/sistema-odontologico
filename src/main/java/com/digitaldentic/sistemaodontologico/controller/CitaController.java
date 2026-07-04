package com.digitaldentic.sistemaodontologico.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitaldentic.sistemaodontologico.Entity.EstadocitaEntity;
import com.digitaldentic.sistemaodontologico.Entity.CitaEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Service.CitaService;
import com.digitaldentic.sistemaodontologico.Service.UsuarioService;

@Controller
@RequestMapping("/cita")
public class CitaController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;

    public CitaController(CitaService citaService, UsuarioService usuarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
    }

    // ===============================
    // FORMULARIO RESERVA
    // ===============================
    @GetMapping("/reservar")
    public String formularioReserva(Model model) {
        if (!model.containsAttribute("cita")) {
            model.addAttribute("cita", new CitaEntity());
        }
        model.addAttribute("doctores", usuarioService.listarDoctores());
        return "cita/reservar";
    }

    // ===============================
    // HORAS DISPONIBLES
    // ===============================
    @GetMapping("/horas-disponibles")
    @ResponseBody
    public List<String> horasDisponibles(@RequestParam Long doctorId, @RequestParam String fecha) {

        UsuarioEntity doctor = usuarioService.buscarPorId(doctorId);
        LocalDate dia = LocalDate.parse(fecha);
        LocalDateTime inicioDia = dia.atStartOfDay();
        LocalDateTime finDia = dia.atTime(LocalTime.MAX);

        List<CitaEntity> citas = citaService.citasDelDia(doctor, inicioDia, finDia);

        List<Integer> horasOcupadas = new ArrayList<>();
        for (CitaEntity c : citas) {
            horasOcupadas.add(c.getFechaHora().getHour());
        }

        List<String> disponibles = new ArrayList<>();
        for (int h = 8; h <= 18; h++) {
            if (!horasOcupadas.contains(h)) {
                disponibles.add(String.format("%02d:00", h));
            }
        }

        return disponibles;
    }

    // ===============================
    // GUARDAR CITA
    // ===============================
    @PostMapping("/guardar")
    public String guardarCita(CitaEntity cita, Model model, @RequestParam Long doctorId) {

        UsuarioEntity doctor = usuarioService.buscarPorId(doctorId);
        cita.setDoctor(doctor);
        cita.setEstado(EstadocitaEntity.RESERVADA);

        if (citaService.existeCita(doctor, cita.getFechaHora())) {
            model.addAttribute("error", "Ese horario ya está reservado");
            model.addAttribute("doctores", usuarioService.listarDoctores());
            model.addAttribute("cita", cita);
            return "cita/reservar";
        }

        citaService.guardar(cita);
        return "redirect:/cita/confirmacion";
    }

    // ===============================
    // PÁGINA DE CONFIRMACIÓN
    // ===============================
    @GetMapping("/confirmacion")
    public String confirmacion() {
        return "cita/confirmacion"; // asegúrate de que exista confirmacion.html en templates/cita
    }

    // ===============================
    // LISTA GENERAL
    // ===============================
    @GetMapping("/lista")
    public String listarTodas(Model model) {
        model.addAttribute("citas", citaService.listarTodas());
        return "cita/lista";
    }

    // ===============================
    // LISTA SOLO DOCTOR
    // ===============================
    @GetMapping("/mis-citas")
    public String listarPorDoctor(Model model, Principal principal) {
        UsuarioEntity doctor = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("citas", citaService.listarPorDoctor(doctor));
        return "cita/lista";
    }

    // ===============================
    // ACTUALIZAR ESTADO
    // ===============================
    @PostMapping("/actualizar-estado")
    public String actualizarEstado(@RequestParam Long id,
                                   @RequestParam EstadocitaEntity estado,
                                   Principal principal) {

        UsuarioEntity usuario = usuarioService.buscarPorEmail(principal.getName());
        CitaEntity cita = citaService.buscarPorId(id);

        if ("DOCTOR".equals(usuario.getRol())) {
            if (!cita.getDoctor().getId().equals(usuario.getId()) || estado != EstadocitaEntity.ATENDIDA) {
                return "redirect:/cita/mis-citas";
            }
        }

        if ("SECRETARIO".equals(usuario.getRol()) || "ADMIN".equals(usuario.getRol())) {
            if (estado != EstadocitaEntity.CANCELADA) {
                return "redirect:/cita/lista";
            }
        }

        citaService.actualizarEstado(id, estado);
        return "DOCTOR".equals(usuario.getRol()) ? "redirect:/cita/mis-citas" : "redirect:/cita/lista";
    }
}