package com.digitaldentic.sistemaodontologico.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.ContenidoEntity;
import com.digitaldentic.sistemaodontologico.Repository.ContenidoRepository;

@Service
public class ContenidoService {

    private final ContenidoRepository repo;

    public ContenidoService(ContenidoRepository repo) {
        this.repo = repo;
    }

    public List<ContenidoEntity> listar() {
        return repo.findAll();
    }

    public ContenidoEntity guardar(ContenidoEntity c) {
        return repo.save(c);
    }
}
