package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;

    // 🔹 Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @NotNull(message = "El usuario es requerido")
    private Usuario usuario;

    // 🔹 Relación con Herramienta
    @ManyToOne
    @JoinColumn(name = "id_herramienta", nullable = false)
    @NotNull(message = "La herramienta es requerida")
    private Herramientas herramienta;

    // 🔹 Relación con Detalle de Herramienta
    @ManyToOne
    @JoinColumn(name = "id_detalle", nullable = false)
    @NotNull(message = "El detalle de la herramienta es requerido")
    private Herramienta_detalle herramientaDetalle;

    // 🔹 NUEVA RELACIÓN CON APRENDIZ
    @ManyToOne
    @JoinColumn(name = "id_aprendiz", nullable = false)
    @NotNull(message = "El aprendiz es requerido")
    private Aprendiz aprendiz;

    // 🔹 Fechas
    @NotNull(message = "La fecha de préstamo es requerida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDateTime fechaPrestamo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    // 🔹 Estado
    @NotNull(message = "El estado es requerido")
    @Pattern(regexp = "^(Activo|Finalizado|Vencido)$",
            message = "El estado debe ser: Activo, Finalizado o Vencido")
    @Column(nullable = false)
    private String estado;

    // 🔹 Constructor vacío
    public Prestamo() {}

    // 🔹 Constructor con parámetros
    public Prestamo(Usuario usuario, Herramientas herramienta, Herramienta_detalle herramientaDetalle,
                    Aprendiz aprendiz, LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion, String estado) {
        this.usuario = usuario;
        this.herramienta = herramienta;
        this.herramientaDetalle = herramientaDetalle;
        this.aprendiz = aprendiz;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    // 🔹 Getters y Setters
    public Integer getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(Integer idPrestamo) { this.idPrestamo = idPrestamo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Herramientas getHerramienta() { return herramienta; }
    public void setHerramienta(Herramientas herramienta) { this.herramienta = herramienta; }

    public Herramienta_detalle getHerramientaDetalle() { return herramientaDetalle; }
    public void setHerramientaDetalle(Herramienta_detalle herramientaDetalle) { this.herramientaDetalle = herramientaDetalle; }

    public Aprendiz getAprendiz() { return aprendiz; }
    public void setAprendiz(Aprendiz aprendiz) { this.aprendiz = aprendiz; }

    public LocalDateTime getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDateTime fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDateTime getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDateTime fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
