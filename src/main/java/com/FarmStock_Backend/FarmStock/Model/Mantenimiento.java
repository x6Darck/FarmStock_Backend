package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDate;

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
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "mantenimiento")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Integer idMantenimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_herramienta", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "La herramienta es requerida")
    private Herramientas herramienta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Herramienta_detalle herramientaDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realizado_por")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @NotBlank(message = "El tipo es requerido")
    @Pattern(regexp = "^(MANTENIMIENTO|DAÑO)$", message = "Tipo debe ser: MANTENIMIENTO o DAÑO")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "La fecha es requerida")
    @Column(name = "fecha_mantenimiento", nullable = false)
    private LocalDate fechaMantenimiento;

    @Pattern(regexp = "^(PENDIENTE|EN_PROCESO|COMPLETADO)$", message = "Estado debe ser: PENDIENTE, EN_PROCESO o COMPLETADO")
    @Column(name = "estado")
    private String estado;

    public Mantenimiento() {}

    // Getters y Setters
    public Integer getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Integer idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public Herramientas getHerramienta() {
        return herramienta;
    }

    public void setHerramienta(Herramientas herramienta) {
        this.herramienta = herramienta;
    }

    public Herramienta_detalle getHerramientaDetalle() {
        return herramientaDetalle;
    }

    public void setHerramientaDetalle(Herramienta_detalle herramientaDetalle) {
        this.herramientaDetalle = herramientaDetalle;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion != null ? descripcion.trim() : null;
    }

    public LocalDate getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(LocalDate fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}