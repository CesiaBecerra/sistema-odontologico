package com.digitaldentic.sistemaodontologico.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.OdontogramaEntity;
import com.digitaldentic.sistemaodontologico.Repository.OdontogramaRepository;


@Service
public class OdontogramaService {

    private final OdontogramaRepository repo;

    public OdontogramaService(OdontogramaRepository repo) {
        this.repo = repo;
    }

    public List<OdontogramaEntity> buscarPorHistorial(Long historialId) {
        return repo.findByHistorialId(historialId);
    }

    public List<OdontogramaEntity> listar() {
        return repo.findAll();
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    public OdontogramaEntity guardar(OdontogramaEntity o) {

    OdontogramaEntity existente =
        repo.findByHistorialIdAndPiezaAndSuperficie(
            o.getHistorial().getId(),
            o.getPieza(),
            o.getSuperficie()
        );

    if (existente != null) {
        existente.setEstado(o.getEstado());
        return repo.save(existente);
    }

    return repo.save(o);
}
}
