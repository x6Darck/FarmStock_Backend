package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "equipos_movimientos")
public class Equipos_Movimientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    @JsonProperty("id_movimiento")
    private Integer idMovimiento;

    @NotBlank(message = "El código del equipo es requerido")
    @Column(name = "codigo_equipo", nullable = false, length = 50)
    @JsonProperty("codigo_equipo")
    private String codigoEquipo;

    @NotNull(message = "El tipo de movimiento es requerido")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    @JsonProperty("tipo_movimiento")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La fecha de movimiento es requerida")
    @Column(name = "fecha_movimiento", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    @NotNull(message = "El registrador es requerido")
    @Column(name = "registrado_por", nullable = false)
    @JsonProperty("registrado_por")
    private Integer registradoPor;

    @Column(name = "observacion", length = 255)
    private String observacion;

    public Equipos_Movimientos() {
    }

    public Equipos_Movimientos(String codigoEquipo, TipoMovimiento tipoMovimiento, 
                               LocalDateTime fechaMovimiento, Integer registradoPor, String observacion) {
        this.codigoEquipo = codigoEquipo;
        this.tipoMovimiento = tipoMovimiento;
        this.fechaMovimiento = fechaMovimiento;
        this.registradoPor = registradoPor;
        this.observacion = observacion;
    }

    // Enum para tipo de movimiento
    public enum TipoMovimiento {
        SALIDA,
        ENTRADA
    }

    // Getters y Setters
    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Integer getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(Integer registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "Equipos_Movimientos{" +
                "idMovimiento=" + idMovimiento +
                ", codigoEquipo='" + codigoEquipo + '\'' +
                ", tipoMovimiento=" + tipoMovimiento +
                ", fechaMovimiento=" + fechaMovimiento +
                ", registradoPor=" + registradoPor +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}
