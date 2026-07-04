package com.digitaldentic.sistemaodontologico.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> roles =
                authentication.getAuthorities();

        for (GrantedAuthority role : roles) {

            if (role.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/dashboard");
                return;
            }

            if (role.getAuthority().equals("ROLE_DOCTOR")) {
                response.sendRedirect("/doctor/dashboard");
                return;
            }

            if (role.getAuthority().equals("ROLE_SECRETARIO")) {
                response.sendRedirect("/secretario/dashboard");
                return;
            }
        }

        response.sendRedirect("/login?error=true");
    }
}
