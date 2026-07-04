package com.digitaldentic.sistemaodontologico.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.digitaldentic.sistemaodontologico.Entity.ConsultaEntity;
import com.digitaldentic.sistemaodontologico.Service.ConsultaService;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/nueva")
    public String nuevaConsulta(Model model) {
        model.addAttribute("consulta", new ConsultaEntity());
        return "consulta/formulario";
    }

    @PostMapping("/guardar")
    public String guardarConsulta(ConsultaEntity consulta) {
        consultaService.guardar(consulta);
        return "redirect:/consultas/enviada";
    }

    @GetMapping("/enviada")
    public String enviada() {
        return "consulta/enviada";
    }
}
