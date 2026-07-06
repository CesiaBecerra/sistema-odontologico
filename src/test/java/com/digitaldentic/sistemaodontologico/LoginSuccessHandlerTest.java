package com.digitaldentic.sistemaodontologico;

import com.digitaldentic.sistemaodontologico.security.LoginSuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

class LoginSuccessHandlerTest {

    private Authentication mockAuth(String role) {

        Authentication auth = mock(Authentication.class);

        Collection authorities = Arrays.asList(
                new SimpleGrantedAuthority(role)
        );

        when(auth.getAuthorities()).thenReturn(authorities);

        return auth;
    }

    @Test
    void testAdminRedirect() throws Exception {

        LoginSuccessHandler handler = new LoginSuccessHandler();

        Authentication auth = mockAuth("ROLE_ADMIN");
        HttpServletResponse response = mock(HttpServletResponse.class);

        handler.onAuthenticationSuccess(null, response, auth);

        verify(response).sendRedirect("/admin/dashboard");
    }

    @Test
    void testDoctorRedirect() throws Exception {

        LoginSuccessHandler handler = new LoginSuccessHandler();

        Authentication auth = mockAuth("ROLE_DOCTOR");
        HttpServletResponse response = mock(HttpServletResponse.class);

        handler.onAuthenticationSuccess(null, response, auth);

        verify(response).sendRedirect("/doctor/dashboard");
    }

    @Test
    void testSecretarioRedirect() throws Exception {

        LoginSuccessHandler handler = new LoginSuccessHandler();

        Authentication auth = mockAuth("ROLE_SECRETARIO");
        HttpServletResponse response = mock(HttpServletResponse.class);

        handler.onAuthenticationSuccess(null, response, auth);

        verify(response).sendRedirect("/secretario/dashboard");
    }

    @Test
    void testDefaultRedirect() throws Exception {

        LoginSuccessHandler handler = new LoginSuccessHandler();

        Authentication auth = mockAuth("ROLE_UNKNOWN");
        HttpServletResponse response = mock(HttpServletResponse.class);

        handler.onAuthenticationSuccess(null, response, auth);

        verify(response).sendRedirect("/login?error=true");
    }
}