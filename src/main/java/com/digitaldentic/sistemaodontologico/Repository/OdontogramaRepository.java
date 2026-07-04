package com.digitaldentic.sistemaodontologico.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldentic.sistemaodontologico.Entity.OdontogramaEntity;

public interface OdontogramaRepository extends JpaRepository<OdontogramaEntity, Long> {

    List<OdontogramaEntity> findByHistorialId(Long historialId);

    OdontogramaEntity findByHistorialIdAndPiezaAndSuperficie(
        Long historialId,
        String pieza,
        String superficie
    );
}