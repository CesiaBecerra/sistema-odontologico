package com.digitaldentic.sistemaodontologico.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monto;
    private String metodoPago;
    private LocalDate fechaPago;

    @Column(unique = true)
    private String numeroComprobante;

    @ManyToOne
    private PacienteEntity paciente;

    @ManyToOne
    private HistorialEntity historial;

    @PrePersist
    public void prePersist(){
        if(fechaPago == null){
            fechaPago = LocalDate.now();
        }
        if(historial != null && paciente == null){
            paciente = historial.getPaciente();
        }
        if(monto == null){
            monto = 0.0;
        }
    }

    // ===== GETTERS Y SETTERS =====

    public Long getId() { return id; }
    // IMPORTANTE: Este método es necesario para los tests unitarios
    public void setId(Long id) { this.id = id; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getNumeroComprobante() { return numeroComprobante; }
    public void setNumeroComprobante(String numeroComprobante) { this.numeroComprobante = numeroComprobante; }

    public PacienteEntity getPaciente() { return paciente; }
    public void setPaciente(PacienteEntity paciente) { this.paciente = paciente; }

    public HistorialEntity getHistorial() { return historial; }
    public void setHistorial(HistorialEntity historial) { this.historial = historial; }
}