package com.digitaldentic.sistemaodontologico.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.PacienteEntity;
import com.digitaldentic.sistemaodontologico.Repository.HistorialRepository;
import com.digitaldentic.sistemaodontologico.Repository.PacienteRepository;
import com.digitaldentic.sistemaodontologico.Repository.UsuarioRepository;

@Service
public class HistorialService {

    private final HistorialRepository repo;
    private final PacienteRepository pacienteRepo;
    private final UsuarioRepository usuarioRepo;

    public HistorialService(HistorialRepository repo,
                            PacienteRepository pacienteRepo,
                            UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.pacienteRepo = pacienteRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public List<HistorialEntity> listar() {
        return repo.findAll();
    }

    public HistorialEntity guardar(HistorialEntity h) {

        // Reconstruir paciente
        Long pacienteId = h.getPaciente().getId();
        PacienteEntity paciente = pacienteRepo.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        h.setPaciente(paciente);

        // Asignar doctor automáticamente
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        var doctor = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        h.setDoctor(doctor);

        // Fecha automática
        if (h.getFecha() == null) {
            h.setFecha(LocalDate.now());
        }

        // Validación de pagos
        double costo = h.getCostoTratamiento() != null ? h.getCostoTratamiento() : 0.0;
        double pagado = h.getMonto() != null ? h.getMonto() : 0.0;

        if (pagado > costo) {
            throw new RuntimeException("El monto pagado no puede ser mayor al costo del tratamiento.");
        }

        double saldo = costo - pagado;
        h.setSaldo(saldo);

        // Estado automático
        if (pagado == 0) {
            h.setEstadoPago("PENDIENTE");
        } else if (saldo == 0) {
            h.setEstadoPago("PAGADO");
        } else {
            h.setEstadoPago("PARCIAL");
        }

        return repo.save(h);
    }

    public void marcarComoPagado(Long id) {
        HistorialEntity h = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));

        h.setEstadoPago("PAGADO");
        h.setSaldo(0.0);
        repo.save(h);
    }

    public List<HistorialEntity> buscarPorPaciente(Long pacienteId) {
        return repo.findByPacienteIdOrderByFechaDesc(pacienteId);
    }

    public HistorialEntity buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }
}