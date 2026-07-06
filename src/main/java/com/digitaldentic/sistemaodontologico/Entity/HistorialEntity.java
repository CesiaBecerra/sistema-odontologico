package com.digitaldentic.sistemaodontologico.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class HistorialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // DATOS DE CONSULTA
    // =========================
    private String motivoConsulta;
    private String sintomas;
    private String diagnostico;
    private String planTratamiento;
    private String observaciones;

    // =========================
    // ANTECEDENTES MÉDICOS
    // =========================
    @Column(nullable = false)
    private boolean diabetes;

    @Column(nullable = false)
    private boolean hipertension;

    @Column(nullable = false)
    private boolean alergias;

    @Column(nullable = false)
    private boolean embarazo;

    @Column(nullable = false)
    private boolean epilepsia;

    @Column(nullable = false)
    private boolean prcardiacos;

    @Column(nullable = false)
    private boolean hepatitis;

    @Column(nullable = false)
    private boolean enfrenales;

    @Column(nullable = false)
    private boolean tbc;

    // =========================
    // ODONTOGRAMA
    // =========================
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String odontogramaAnotacion;

    // =========================
    // COSTOS Y PAGOS
    // =========================
    private Double costoTratamiento = 0.0;
    private Double monto = 0.0; // monto pagado
    private Double saldo = 0.0;

    @Column(nullable = false)
    private String estadoPago = "PENDIENTE";

    // =========================
    // FECHA
    // =========================
    private LocalDate fecha;

    // =========================
    // RELACIONES
    // =========================
    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacienteEntity paciente;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private UsuarioEntity doctor;

    // =========================
    // MÉTODOS AUTOMÁTICOS
    // =========================
    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        calcularSaldo();
    }

    @PreUpdate
    public void preUpdate() {
        calcularSaldo();
    }

    private void calcularSaldo() {
        double costo = (costoTratamiento != null) ? costoTratamiento : 0.0;
        double pago = (monto != null) ? monto : 0.0;

        saldo = Math.max(costo - pago, 0.0);

        if (saldo == 0.0) {
            estadoPago = "PAGADO";
        } else if (pago > 0) {
            estadoPago = "PARCIAL";
        } else {
            estadoPago = "PENDIENTE";
        }
    }

    public boolean isPagado() {
        return "PAGADO".equalsIgnoreCase(estadoPago);
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================
    public Long getId() { return id; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getPlanTratamiento() { return planTratamiento; }
    public void setPlanTratamiento(String planTratamiento) { this.planTratamiento = planTratamiento; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public boolean isDiabetes() { return diabetes; }
    public void setDiabetes(boolean diabetes) { this.diabetes = diabetes; }

    public boolean isHipertension() { return hipertension; }
    public void setHipertension(boolean hipertension) { this.hipertension = hipertension; }

    public boolean isAlergias() { return alergias; }
    public void setAlergias(boolean alergias) { this.alergias = alergias; }

    public boolean isEmbarazo() { return embarazo; }
    public void setEmbarazo(boolean embarazo) { this.embarazo = embarazo; }

    public boolean isEpilepsia() { return epilepsia; }
    public void setEpilepsia(boolean epilepsia) { this.epilepsia = epilepsia; }

    public boolean isPrcardiacos() { return prcardiacos; }
    public void setPrcardiacos(boolean prcardiacos) { this.prcardiacos = prcardiacos; }

    public boolean isHepatitis() { return hepatitis; }
    public void setHepatitis(boolean hepatitis) { this.hepatitis = hepatitis; }

    public boolean isEnfrenales() { return enfrenales; }
    public void setEnfrenales(boolean enfrenales) { this.enfrenales = enfrenales; }

    public boolean isTbc() { return tbc; }
    public void setTbc(boolean tbc) { this.tbc = tbc; }

    public Double getCostoTratamiento() { return costoTratamiento; }
    public void setCostoTratamiento(Double costoTratamiento) { this.costoTratamiento = costoTratamiento; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public PacienteEntity getPaciente() { return paciente; }
    public void setPaciente(PacienteEntity paciente) { this.paciente = paciente; }

    public UsuarioEntity getDoctor() { return doctor; }
    public void setDoctor(UsuarioEntity doctor) { this.doctor = doctor; }

    public String getOdontogramaAnotacion() { return odontogramaAnotacion; }
    public void setOdontogramaAnotacion(String odontogramaAnotacion) { this.odontogramaAnotacion = odontogramaAnotacion; }

    // Añade esto en HistorialEntity.java
    public void setId(Long id) {
        this.id = id;
    }

}