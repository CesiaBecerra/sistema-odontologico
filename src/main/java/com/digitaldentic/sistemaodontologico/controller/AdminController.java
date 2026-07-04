package com.digitaldentic.sistemaodontologico.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;




@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
     @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cierra la sesión
        return "redirect:/login"; // Redirige a la página de login
    }
}
