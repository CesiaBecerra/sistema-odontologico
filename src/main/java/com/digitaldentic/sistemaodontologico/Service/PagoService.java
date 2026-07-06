package com.digitaldentic.sistemaodontologico.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.CajaEntity;
import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.PagoEntity;
import com.digitaldentic.sistemaodontologico.Repository.CajaRepository;
import com.digitaldentic.sistemaodontologico.Repository.HistorialRepository;
import com.digitaldentic.sistemaodontologico.Repository.PagoRepository;

@Service
public class PagoService {

    private final PagoRepository repo;
    private final HistorialRepository historialRepo;
    private final CajaRepository cajaRepo;

    public PagoService(PagoRepository repo,
                       HistorialRepository historialRepo,
                       CajaRepository cajaRepo) {
        this.repo = repo;
        this.historialRepo = historialRepo;
        this.cajaRepo = cajaRepo;
    }

    public PagoEntity guardar(PagoEntity p) {

        HistorialEntity historial = historialRepo
                .findById(p.getHistorial().getId())
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));

        Double totalPagado = repo.totalPagadoPorHistorial(historial.getId());
        if (totalPagado == null) {
            totalPagado = 0.0;
        }

        Double restante = historial.getCostoTratamiento() - totalPagado;

        if (p.getMonto() == null || p.getMonto() <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0.");
        }

        if (p.getMonto() > restante) {
            throw new RuntimeException("No puede pagar más de lo que debe.");
        }

        Long ultimoId = repo.ultimoId();
        Long siguiente = (ultimoId == null ? 1 : ultimoId + 1);

        p.setNumeroComprobante("COMP-2026-" + String.format("%05d", siguiente));
        p.setFechaPago(LocalDate.now());

        // Actualizar monto acumulado del historial
        historial.setMonto(totalPagado + p.getMonto());

        historialRepo.save(historial);

        return repo.save(p);
    }

    public List<PagoEntity> listar() {
        return repo.findAll();
    }

    public Double total() {
        return repo.findAll().stream()
                .mapToDouble(p -> p.getMonto() != null ? p.getMonto() : 0.0)
                .sum();
    }

    public List<PagoEntity> buscarPorHistorial(Long historialId) {
        return repo.findByHistorialId(historialId);
    }

    public PagoEntity findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Double deudaHistorial(Long historialId) {

        HistorialEntity historial = historialRepo
                .findById(historialId)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));

        Double pagado = repo.totalPagadoPorHistorial(historialId);

        return historial.getCostoTratamiento()
                - (pagado == null ? 0.0 : pagado);
    }

    public List<PagoEntity> pagosPorRango(LocalDate inicio, LocalDate fin) {
        return repo.findByFechaPagoBetween(inicio, fin);
    }

    public Double totalPorRango(LocalDate inicio, LocalDate fin) {
        return repo.findByFechaPagoBetween(inicio, fin).stream()
                .mapToDouble(p -> p.getMonto() != null ? p.getMonto() : 0.0)
                .sum();
    }

    public List<PagoEntity> pagosHoy() {
        return repo.findByFechaPago(LocalDate.now());
    }

    public Double totalHoy() {
        return repo.findByFechaPago(LocalDate.now()).stream()
                .mapToDouble(p -> p.getMonto() != null ? p.getMonto() : 0.0)
                .sum();
    }

    public void cerrarCaja() {

        Double totalHoy = totalHoy();

        CajaEntity cierre = new CajaEntity();
        cierre.setFecha(LocalDate.now());
        cierre.setTotalDia(totalHoy);

        cajaRepo.save(cierre);
    }

    public Map<String, Long> conteoMetodo() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getMetodoPago() == null
                                ? "SIN MÉTODO"
                                : p.getMetodoPago(),
                        Collectors.counting()
                ));
    }

    public PagoEntity buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }
}