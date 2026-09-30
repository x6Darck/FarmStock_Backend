package com.FarmStock_Backend.FarmStock.DTO;

/**
 * DTO para estadísticas de daños y mantenimientos de una herramienta específica.
 * Usado para reportes y análisis de estado de herramientas.
 */
public class EstadisticasHerramientaDTO {

    private Integer idHerramienta;
    private String nombreHerramienta;
    private Long totalPrestamos;
    private Long totalDanos;
    private Long totalMantenimientos;

    public EstadisticasHerramientaDTO() {
    }

    public EstadisticasHerramientaDTO(Integer idHerramienta, String nombreHerramienta, 
                                      Long totalPrestamos, Long totalDanos, Long totalMantenimientos) {
        this.idHerramienta = idHerramienta;
        this.nombreHerramienta = nombreHerramienta;
        this.totalPrestamos = totalPrestamos;
        this.totalDanos = totalDanos;
        this.totalMantenimientos = totalMantenimientos;
    }

    // Getters y Setters
    public Integer getIdHerramienta() {
        return idHerramienta;
    }

    public void setIdHerramienta(Integer idHerramienta) {
        this.idHerramienta = idHerramienta;
    }

    public String getNombreHerramienta() {
        return nombreHerramienta;
    }

    public void setNombreHerramienta(String nombreHerramienta) {
        this.nombreHerramienta = nombreHerramienta;
    }

    public Long getTotalPrestamos() {
        return totalPrestamos;
    }

    public void setTotalPrestamos(Long totalPrestamos) {
        this.totalPrestamos = totalPrestamos;
    }

    public Long getTotalDanos() {
        return totalDanos;
    }

    public void setTotalDanos(Long totalDanos) {
        this.totalDanos = totalDanos;
    }

    public Long getTotalMantenimientos() {
        return totalMantenimientos;
    }

    public void setTotalMantenimientos(Long totalMantenimientos) {
        this.totalMantenimientos = totalMantenimientos;
    }

    @Override
    public String toString() {
        return "EstadisticasHerramientaDTO{" +
                "idHerramienta=" + idHerramienta +
                ", nombreHerramienta='" + nombreHerramienta + '\'' +
                ", totalPrestamos=" + totalPrestamos +
                ", totalDanos=" + totalDanos +
                ", totalMantenimientos=" + totalMantenimientos +
                '}';
    }
}
