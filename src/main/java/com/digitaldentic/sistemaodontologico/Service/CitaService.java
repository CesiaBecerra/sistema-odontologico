package com.digitaldentic.sistemaodontologico.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.EstadocitaEntity;
import com.digitaldentic.sistemaodontologico.Entity.CitaEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;
import com.digitaldentic.sistemaodontologico.Repository.CitaRepository;

@Service
public class CitaService {

    private final CitaRepository repo;

    public CitaService(CitaRepository repo) {
        this.repo = repo;
    }

    public List<CitaEntity> listarTodas() {
        return repo.findAll();
    }

    public List<CitaEntity> listarPorDoctor(UsuarioEntity doctor) {
        return repo.findByDoctor(doctor);
    }

    public boolean existeCita(UsuarioEntity doctor, LocalDateTime fechaHora) {
        return repo.existsByDoctorAndFechaHora(doctor, fechaHora);
    }

    public CitaEntity guardar(CitaEntity c) {
        return repo.save(c);
    }

    public List<CitaEntity> citasDelDia(
            UsuarioEntity doctor,
            LocalDateTime inicio,
            LocalDateTime fin) {
        return repo.findByDoctorAndFechaHoraBetween(doctor, inicio, fin);
    }

    // ===============================
    // NUEVOS MÉTODOS
    // ===============================

    public CitaEntity buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    public void actualizarEstado(Long id, EstadocitaEntity estado) {
        CitaEntity cita = buscarPorId(id);
        cita.setEstado(estado);
        repo.save(cita);
    }
}