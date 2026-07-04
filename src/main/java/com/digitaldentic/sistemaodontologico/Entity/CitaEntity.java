package com.digitaldentic.sistemaodontologico.Entity;

import java.time.LocalDateTime;



import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CitaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;



    private String nombrePaciente;
    private String telefono;
    private String motivoConsulta;
    @Enumerated(EnumType.STRING)
private EstadocitaEntity estado;
    @ManyToOne
private PacienteEntity paciente;
    

    @ManyToOne
    private UsuarioEntity doctor;

    // GETTERS Y SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public EstadocitaEntity getEstado() { return estado; }
    public void setEstado(EstadocitaEntity estado) { this.estado = estado; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public UsuarioEntity getDoctor() { return doctor; }
    public void setDoctor(UsuarioEntity doctor) { this.doctor = doctor; }
    public PacienteEntity getPaciente() { return paciente; }
    public void setPaciente(PacienteEntity paciente) { this.paciente = paciente; }
}