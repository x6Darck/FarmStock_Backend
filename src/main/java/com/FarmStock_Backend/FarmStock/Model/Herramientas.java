package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "herramienta")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Herramientas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_herramienta")
    private Integer idHerramienta;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotBlank(message = "El estado no puede estar vacío")
    @Column(name = "estado")
    private String estado;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Column(name = "tipo")
    private String tipo;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Column(name = "ubicacion")
    private String ubicacion;

    @NotBlank(message = "El número de lote no puede estar vacío")
    @Column(name = "numero_lote")
    @JsonProperty("numero_lote")
    private String numeroLote;

    @NotNull(message = "La cantidad no puede estar vacía")
    @Column(name = "cantidad")
    private Integer cantidad;

    @NotNull(message = "La fecha de registro no puede ser nula")
    @PastOrPresent(message = "La fecha de registro no puede ser futura")
    @Column(name = "fecha_registro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "contador_prestamos")
    @JsonProperty("contador_prestamos")
    private Integer contadorPrestamos = 0;

    public Herramientas() {}

    // getters / setters
    public Integer getIdHerramienta() { return idHerramienta; }
    public void setIdHerramienta(Integer idHerramienta) { this.idHerramienta = idHerramienta; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Integer getContadorPrestamos() { return contadorPrestamos; }
    public void setContadorPrestamos(Integer contadorPrestamos) { this.contadorPrestamos = contadorPrestamos; }
}