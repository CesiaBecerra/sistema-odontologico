package com.digitaldentic.sistemaodontologico.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldentic.sistemaodontologico.Entity.ContenidoEntity;

public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Long> {
}
