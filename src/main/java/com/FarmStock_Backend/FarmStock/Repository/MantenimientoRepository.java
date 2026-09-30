package com.FarmStock_Backend.FarmStock.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.FarmStock_Backend.FarmStock.Model.Mantenimiento;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    /**
     * Cuenta la cantidad de registros de un tipo específico (MANTENIMIENTO o DAÑO)
     * para una herramienta específica.
     */
    @Query("SELECT COUNT(m) FROM Mantenimiento m WHERE m.herramienta.idHerramienta = :idHerramienta AND m.tipo = :tipo")
    Long contarPorHerramientaYTipo(@Param("idHerramienta") Integer idHerramienta, @Param("tipo") String tipo);

    /**
     * Obtiene todos los mantenimientos/daños de una herramienta específica.
     */
    List<Mantenimiento> findByHerramienta_IdHerramienta(Integer idHerramienta);

    /**
     * Obtiene todos los registros de un tipo específico.
     */
    List<Mantenimiento> findByTipo(String tipo);

    /**
     * Obtiene todos los mantenimientos/daños realizados por un usuario específico.
     */
    List<Mantenimiento> findByUsuario_IdUsuario(Integer idUsuario);

    /**
     * Cuenta solo los daños de una herramienta específica.
     * Optimizado con query específica para mejor rendimiento.
     */
    @Query("SELECT COUNT(m) FROM Mantenimiento m WHERE m.herramienta.idHerramienta = :idHerramienta AND m.tipo = 'DAÑO'")
    Long contarDanosPorHerramienta(@Param("idHerramienta") Integer idHerramienta);

    /**
     * Cuenta solo los mantenimientos de una herramienta específica.
     * Optimizado con query específica para mejor rendimiento.
     */
    @Query("SELECT COUNT(m) FROM Mantenimiento m WHERE m.herramienta.idHerramienta = :idHerramienta AND m.tipo = 'MANTENIMIENTO'")
    Long contarMantenimientosPorHerramienta(@Param("idHerramienta") Integer idHerramienta);

    /**
     * Cuenta los daños de una unidad física específica por código único.
     */
    @Query("SELECT COUNT(m) FROM Mantenimiento m WHERE m.herramientaDetalle.codigoUnico = :codigoUnico AND m.tipo = 'DAÑO'")
    Long contarDanosPorCodigoUnico(@Param("codigoUnico") String codigoUnico);

    /**
     * Cuenta los mantenimientos de una unidad física específica por código único.
     */
    @Query("SELECT COUNT(m) FROM Mantenimiento m WHERE m.herramientaDetalle.codigoUnico = :codigoUnico AND m.tipo = 'MANTENIMIENTO'")
    Long contarMantenimientosPorCodigoUnico(@Param("codigoUnico") String codigoUnico);
}