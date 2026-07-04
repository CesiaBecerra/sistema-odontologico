package com.digitaldentic.sistemaodontologico.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldentic.sistemaodontologico.Entity.CajaEntity;

public interface CajaRepository extends JpaRepository<CajaEntity, Long> {
}