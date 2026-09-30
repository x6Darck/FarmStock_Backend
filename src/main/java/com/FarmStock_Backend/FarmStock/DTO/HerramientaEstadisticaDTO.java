package com.FarmStock_Backend.FarmStock.DTO;

/**
 * DTO para retornar estadísticas de herramientas.
 * Incluye: id, nombre, cantidad de préstamos, daños y mantenimientos.
 */
public class HerramientaEstadisticaDTO {

    private Integer idHerramienta;
    private String nombre;
    private Long prestamos;
    private Long danos;
    private Long mantenimientos;

    public HerramientaEstadisticaDTO() {}

    public HerramientaEstadisticaDTO(Integer idHerramienta, String nombre, Long prestamos, Long danos, Long mantenimientos) {
        this.idHerramienta = idHerramienta;
        this.nombre = nombre;
        this.prestamos = prestamos;
        this.danos = danos;
        this.mantenimientos = mantenimientos;
    }

    // Getters y Setters
    public Integer getIdHerramienta() { return idHerramienta; }
    public void setIdHerramienta(Integer idHerramienta) { this.idHerramienta = idHerramienta; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getPrestamos() { return prestamos; }
    public void setPrestamos(Long prestamos) { this.prestamos = prestamos; }

    public Long getDanos() { return danos; }
    public void setDanos(Long danos) { this.danos = danos; }

    public Long getMantenimientos() { return mantenimientos; }
    public void setMantenimientos(Long mantenimientos) { this.mantenimientos = mantenimientos; }
}
