package com.digitaldentic.sistemaodontologico.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.digitaldentic.sistemaodontologico.Entity.PagoEntity;

public interface PagoRepository extends JpaRepository<PagoEntity, Long> {

    List<PagoEntity> findByHistorialId(Long historialId);

    List<PagoEntity> findByFechaPago(LocalDate fechaPago);

    List<PagoEntity> findByFechaPagoBetween(LocalDate inicio, LocalDate fin);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM PagoEntity p WHERE p.historial.id = :historialId")
    Double totalPagadoPorHistorial(Long historialId);

    @Query("SELECT MAX(p.id) FROM PagoEntity p")
    Long ultimoId();
}