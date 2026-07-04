package com.digitaldentic.sistemaodontologico.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRedirectController {

    @GetMapping("/redirect")
    public String redirectPorRol(Authentication auth) {

        for (GrantedAuthority role : auth.getAuthorities()) {

            if (role.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            }

            if (role.getAuthority().equals("ROLE_DOCTOR")) {
                return "redirect:/doctor/dashboard";
            }

            if (role.getAuthority().equals("ROLE_SECRETARIO")) {
                return "redirect:/secretario/dashboard";
            }
        }

        return "redirect:/login";
    }
}
