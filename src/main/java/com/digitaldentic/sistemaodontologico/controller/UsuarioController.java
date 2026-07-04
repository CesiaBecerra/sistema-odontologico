package com.digitaldentic.sistemaodontologico.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioEntity());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(UsuarioEntity usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios/lista";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "usuarios/lista";
    }

    @GetMapping("/editar/{id}")
public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("usuario", usuarioService.buscar(id));
    return "usuarios/form";
}

@GetMapping("/eliminar/{id}")
public String eliminar(@PathVariable Long id) {
    usuarioService.eliminar(id);
    return "redirect:/usuarios/lista";
}
}
