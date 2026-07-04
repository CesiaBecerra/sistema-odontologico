package com.digitaldentic.sistemaodontologico.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.RolEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // LISTAR TODOS
    public List<UsuarioEntity> listar() {
        return repo.findAll();
    }

    // GUARDAR
    public UsuarioEntity guardar(UsuarioEntity u) {

        if (u.getId() == null) {
            u.setPassword(encoder.encode(u.getPassword()));
        } else {
            UsuarioEntity bd = repo.findById(u.getId()).orElse(null);

            if (bd != null) {
                if (u.getPassword() == null || u.getPassword().isBlank()) {
                    u.setPassword(bd.getPassword());
                } else {
                    u.setPassword(encoder.encode(u.getPassword()));
                }
            }
        }

        return repo.save(u);
    }

    // BUSCAR POR ID
    public UsuarioEntity buscar(Long id) {
        return repo.findById(id).orElse(null);
    }

    public UsuarioEntity buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    // 🔥 ESTE ES EL QUE TE FALTABA
    public UsuarioEntity buscarPorEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    // FILTROS
    public List<UsuarioEntity> listarDoctores() {
        return repo.findByRol(RolEntity.DOCTOR);
    }

    public List<UsuarioEntity> listarSecretarios() {
        return repo.findByRol(RolEntity.SECRETARIO);
    }

    // ELIMINAR
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}