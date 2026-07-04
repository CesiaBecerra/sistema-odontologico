package com.digitaldentic.sistemaodontologico.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;

public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {

    List<PacienteEntity> findByNombresContainingIgnoreCase(String nombres);

    List<PacienteEntity> findByApellidosContainingIgnoreCase(String apellidos);

    Optional<PacienteEntity> findByDni(String dni);

    @Query("""
        SELECT DISTINCT c.paciente
        FROM CitaEntity c
        WHERE c.doctor.id = :doctorId
        AND c.estado = 'ATENDIDA'
    """)
    List<PacienteEntity> listarPacientesPorDoctor(@Param("doctorId") Long doctorId);
}