package com.digitaldentic.sistemaodontologico.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;





@Controller
@RequestMapping("/secretario")
public class SecretarioController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "secretario/dashboard";
    }
}
