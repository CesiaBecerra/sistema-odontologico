package com.digitaldentic.sistemaodontologico.Entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"historial_id", "pieza", "superficie"}
    )
)
public class OdontogramaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pieza;
    
    @Column(nullable = false)
    private String superficie; // O, M, D, V, L

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOdontogramaEntity estado;

    @Column(length = 500)
    private String tratamiento;

    @ManyToOne
    @JoinColumn(name = "historial_id", nullable = false)
    private HistorialEntity historial;
    
    @Lob
@Column(columnDefinition = "LONGTEXT")
private String odontogramaAnotacion;

public String getOdontogramaAnotacion() {
    return odontogramaAnotacion;
}

public void setOdontogramaAnotacion(String odontogramaAnotacion) {
    this.odontogramaAnotacion = odontogramaAnotacion;
}

    // ===== GETTERS =====

    public Long getId() { return id; }
    public String getPieza() { return pieza; }
    public String getSuperficie() { return superficie; }
    public EstadoOdontogramaEntity getEstado() { return estado; }
    public String getTratamiento() { return tratamiento; }
    public HistorialEntity getHistorial() { return historial; }

    // ===== SETTERS =====

    public void setId(Long id) { this.id = id; }
    public void setPieza(String pieza) { this.pieza = pieza; }
    public void setSuperficie(String superficie) { this.superficie = superficie; }
    public void setEstado(EstadoOdontogramaEntity estado) { this.estado = estado; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public void setHistorial(HistorialEntity historial) { this.historial = historial; }
}