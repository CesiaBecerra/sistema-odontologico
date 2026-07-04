package com.digitaldentic.sistemaodontologico.config;

/*import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;*/
import org.springframework.context.annotation.Configuration;
/*import org.springframework.security.crypto.password.PasswordEncoder;

import com.digitaldentic.sistemaodontologico.entity.RolEntity;
import com.digitaldentic.sistemaodontologico.entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.repository.UsuarioRepository;*/

@Configuration
public class SeederAdmin {

    /*
    @Bean
    CommandLineRunner crearAdmin(
            UsuarioRepository repo,
            PasswordEncoder encoder) {

        return args -> {

            // 🔎 verificar si existe admin
            if (repo.findByEmail("admin@test.com").isEmpty()) {

                UsuarioEntity admin = new UsuarioEntity();
                admin.setNombre("Admin General");
                admin.setEmail("admin@test.com");
                admin.setPassword(encoder.encode("123"));
                admin.setRol(RolEntity.ADMIN);
                admin.setActivo(true);

                repo.save(admin);

                System.out.println("🔥 ADMIN CREADO AUTOMATICAMENTE");
            }
        };
    }
    */
}