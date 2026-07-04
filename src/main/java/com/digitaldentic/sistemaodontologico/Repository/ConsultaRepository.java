package com.digitaldentic.sistemaodontologico.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldentic.sistemaodontologico.Entity.ConsultaEntity;

public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Long> {
}
