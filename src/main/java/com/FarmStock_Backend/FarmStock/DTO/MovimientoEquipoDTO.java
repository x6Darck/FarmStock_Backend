package com.FarmStock_Backend.FarmStock.DTO;

import com.FarmStock_Backend.FarmStock.Model.Equipos_Movimientos.TipoMovimiento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * DTO para devolver el historial de movimientos con la cédula del usuario en lugar del ID
 */
public class MovimientoEquipoDTO {
    
    private Long idMovimiento;
    private String codigoEquipo;
    private TipoMovimiento tipoMovimiento;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaMovimiento;
    
    private String cedulaUsuario;  // Cédula en lugar de ID
    private String nombreUsuario;  // Nombre completo del usuario
    private String observacion;

    // Constructor vacío
    public MovimientoEquipoDTO() {
    }

    // Constructor completo
    public MovimientoEquipoDTO(Long idMovimiento, String codigoEquipo, TipoMovimiento tipoMovimiento,
                               LocalDateTime fechaMovimiento, String cedulaUsuario, String nombreUsuario,
                               String observacion) {
        this.idMovimiento = idMovimiento;
        this.codigoEquipo = codigoEquipo;
        this.tipoMovimiento = tipoMovimiento;
        this.fechaMovimiento = fechaMovimiento;
        this.cedulaUsuario = cedulaUsuario;
        this.nombreUsuario = nombreUsuario;
        this.observacion = observacion;
    }

    // Getters y Setters
    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
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

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
