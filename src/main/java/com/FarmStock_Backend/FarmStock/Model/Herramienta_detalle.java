package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "herramienta_detalle")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Herramienta_detalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_herramienta", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Herramientas herramienta;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_-]+$")
    @Column(name = "codigo_unico", length = 50, nullable = false, unique = true)
    private String codigoUnico;

    @NotBlank
    @Pattern(regexp = "^(Disponible|No_disponible|Mantenimiento)$")
    @Column(name = "estado")
    private String estado;

    @NotNull
    @Column(name = "disponible")
    private Boolean disponible;
    
    

    @NotNull
    @PastOrPresent
    @Column(name = "fecha_ingreso")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    @Column(name = "contador_prestamos")
    private Integer contadorPrestamos = 0;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    public Herramienta_detalle() {}

    // getters / setters
    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }
    public Herramientas getHerramienta() { return herramienta; }
    public void setHerramienta(Herramientas herramienta) { this.herramienta = herramienta; }
    public String getCodigoUnico() { return codigoUnico; }
    public void setCodigoUnico(String codigoUnico) { this.codigoUnico = codigoUnico; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public Integer getContadorPrestamos() { return contadorPrestamos; }
    public void setContadorPrestamos(Integer contadorPrestamos) { this.contadorPrestamos = contadorPrestamos; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}