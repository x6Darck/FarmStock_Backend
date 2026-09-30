package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "reportedanio")
public class ReporteDanio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danio")
    private Integer idDanio;

    @NotNull
    @Column(name = "id_herramienta")
    private Integer idHerramienta;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_reporte")
    private LocalDate fechaReporte;

    @Column(name = "reportado_por")
    private Integer reportadoPor; // id_usuario

    @Column(name = "id_detalle")
    private Integer idDetalle;

    public ReporteDanio() {}

    public Integer getIdDanio() {
        return idDanio;
    }

    public void setIdDanio(Integer idDanio) {
        this.idDanio = idDanio;
    }

    public Integer getIdHerramienta() {
        return idHerramienta;
    }

    public void setIdHerramienta(Integer idHerramienta) {
        this.idHerramienta = idHerramienta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion != null ? descripcion.trim() : null;
    }

    public LocalDate getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDate fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public Integer getReportadoPor() {
        return reportadoPor;
    }

    public void setReportadoPor(Integer reportadoPor) {
        this.reportadoPor = reportadoPor;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }
}