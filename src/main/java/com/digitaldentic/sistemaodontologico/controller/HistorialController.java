package com.digitaldentic.sistemaodontologico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Service.HistorialService;
import com.digitaldentic.sistemaodontologico.Service.PacienteService;

@Controller
@RequestMapping("/historial")
public class HistorialController {

    private final HistorialService historialService;
    private final PacienteService pacienteService;

    public HistorialController(HistorialService historialService,
                               PacienteService pacienteService) {
        this.historialService = historialService;
        this.pacienteService = pacienteService;
    }

    // Ver historial por paciente
    @GetMapping("/paciente/{id}")
    public String verHistorial(@PathVariable Long id, Model model) {

        var paciente = pacienteService.buscarPorId(id);
        if (paciente == null) return "redirect:/pacientes";

        model.addAttribute("paciente", paciente);
        model.addAttribute("historial", historialService.buscarPorPaciente(id));
        return "historial/lista";
    }

    // Formulario nuevo historial
    @GetMapping("/nuevo/{pacienteId}")
    public String nuevo(@PathVariable Long pacienteId, Model model) {

        var paciente = pacienteService.buscarPorId(pacienteId);
        if (paciente == null) return "redirect:/pacientes";

        HistorialEntity h = new HistorialEntity();
        h.setPaciente(paciente);

        model.addAttribute("historial", h);
        return "historial/form";
    }

    // Guardar historial con manejo de errores
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute HistorialEntity h, Model model) {
        try {
            historialService.guardar(h);
            return "redirect:/historial/paciente/" + h.getPaciente().getId();
        } catch (Exception e) {
            // Enviar error al formulario
            model.addAttribute("historial", h);
            model.addAttribute("error", e.getMessage());
            return "historial/form";
        }
    }

    // Marcar como pagado
    @PostMapping("/pagar/{id}")
    public String pagar(@PathVariable Long id) {

        HistorialEntity h = historialService.buscarPorId(id);
        if (h == null) return "redirect:/pacientes";

        historialService.marcarComoPagado(id);

        return "redirect:/historial/paciente/" + h.getPaciente().getId();
    }

    // Ver detalle completo
    @GetMapping("/ver/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {

        HistorialEntity historial = historialService.buscarPorId(id);

        if (historial == null) {
            return "redirect:/pacientes";
        }

        model.addAttribute("historial", historial);
        return "historial/detalle";
    }
}