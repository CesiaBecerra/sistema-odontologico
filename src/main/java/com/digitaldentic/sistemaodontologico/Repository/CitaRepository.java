package com.digitaldentic.sistemaodontologico.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldentic.sistemaodontologico.Entity.CitaEntity;
import com.digitaldentic.sistemaodontologico.Entity.UsuarioEntity;

public interface CitaRepository extends JpaRepository<CitaEntity, Long> {

    boolean existsByDoctorAndFechaHora(UsuarioEntity doctor, LocalDateTime fechaHora);

    List<CitaEntity> findByDoctor(UsuarioEntity doctor);

    List<CitaEntity> findByDoctorAndFechaHoraBetween(
        UsuarioEntity doctor,
        LocalDateTime inicio,   
        LocalDateTime fin
    );
}