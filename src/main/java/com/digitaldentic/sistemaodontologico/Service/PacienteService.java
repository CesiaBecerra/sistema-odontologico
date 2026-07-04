package com.digitaldentic.sistemaodontologico.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;
import com.digitaldentic.sistemaodontologico.Repository.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository repo;

    public PacienteService(PacienteRepository repo) {
        this.repo = repo;
    }

    public List<PacienteEntity> listar() {
        return repo.findAll();
    }

    public List<PacienteEntity> listarPacientesPorDoctor(Long doctorId) {
        return repo.listarPacientesPorDoctor(doctorId);
    }

    public PacienteEntity guardar(PacienteEntity p) {

        Optional<PacienteEntity> existente = repo.findByDni(p.getDni());

        if (existente.isPresent() &&
            !existente.get().getId().equals(p.getId())) {

            throw new RuntimeException("El DNI ya existe");
        }

        return repo.save(p);
    }

    public PacienteEntity buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public List<PacienteEntity> buscarPorNombre(String nombre) {
        return repo.findByNombresContainingIgnoreCase(nombre);
    }

    public PacienteEntity buscarPorDni(String dni) {
        return repo.findByDni(dni).orElse(null);
    }
}