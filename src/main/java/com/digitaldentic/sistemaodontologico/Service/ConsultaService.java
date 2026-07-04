package com.digitaldentic.sistemaodontologico.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.ConsultaEntity;
import com.digitaldentic.sistemaodontologico.Repository.ConsultaRepository;

@Service
public class ConsultaService {

    private final ConsultaRepository repo;

    public ConsultaService(ConsultaRepository repo) {
        this.repo = repo;
    }

    public List<ConsultaEntity> listar() {
        return repo.findAll();
    }

    public ConsultaEntity guardar(ConsultaEntity c) {
        return repo.save(c);
    }
}
