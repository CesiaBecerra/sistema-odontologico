package com.digitaldentic.sistemaodontologico.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldentic.sistemaodontologico.Entity.RolEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    List<UsuarioEntity> findByRol(RolEntity rol);

    Optional<UsuarioEntity> findByEmail(String email);
   
}