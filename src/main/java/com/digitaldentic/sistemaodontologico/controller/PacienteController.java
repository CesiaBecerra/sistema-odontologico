package com.digitaldentic.sistemaodontologico.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Service.PacienteService;
import com.digitaldentic.sistemaodontologico.Service.UsuarioService;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;
    private final UsuarioService usuarioService;

    public PacienteController(PacienteService service,
                              UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    // LISTAR PACIENTES
    @GetMapping
    public String listar(Model model, Authentication auth) {

        UsuarioEntity usuario = usuarioService.buscarPorEmail(auth.getName());

        if (usuario.getRol().equals("DOCTOR")) {
            model.addAttribute("lista",
                    service.listarPacientesPorDoctor(usuario.getId()));
        } else {
            model.addAttribute("lista", service.listar());
        }

        return "pacientes/lista";
    }

    // NUEVO PACIENTE
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("paciente", new PacienteEntity());
        return "pacientes/form";
    }

    // GUARDAR PACIENTE CON CAPTURA DE ERROR POR DNI DUPLICADO
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PacienteEntity paciente, Model model) {
        try {
            service.guardar(paciente);
            return "redirect:/pacientes";
        } catch (RuntimeException e) {
            // Captura error si el DNI ya existe
            model.addAttribute("error", e.getMessage());
            model.addAttribute("paciente", paciente); // rellena el formulario con datos
            return "pacientes/form";
        }
    }

    // BUSCAR PACIENTE POR NOMBRE
    @GetMapping("/buscar")
    public String buscarPacientePorNombre(@RequestParam String nombre, Model model) {
        model.addAttribute("lista", service.buscarPorNombre(nombre));
        return "pacientes/lista";
    }

    // ELIMINAR PACIENTE
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/pacientes";
    }
}