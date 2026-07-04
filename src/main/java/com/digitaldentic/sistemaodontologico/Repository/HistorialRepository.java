package com.digitaldentic.sistemaodontologico.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;

public interface HistorialRepository extends JpaRepository<HistorialEntity, Long> {

    // Evolución clínica ordenada por fecha descendente
    List<HistorialEntity> findByPacienteIdOrderByFechaDesc(Long pacienteId);
     
    
}