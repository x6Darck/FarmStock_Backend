package com.FarmStock_Backend.FarmStock.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.FarmStock_Backend.FarmStock.Model.Equipos_Movimientos;
import com.FarmStock_Backend.FarmStock.Model.Equipos_Movimientos.TipoMovimiento;

@Repository
public interface Equipos_MovimientosRepository extends JpaRepository<Equipos_Movimientos, Integer> {

    /**
     * Obtiene todos los movimientos de un equipo específico, ordenados por fecha descendente.
     */
    List<Equipos_Movimientos> findByCodigoEquipoOrderByFechaMovimientoDesc(String codigoEquipo);

    /**
     * Obtiene el último movimiento de un equipo específico.
     */
    @Query("SELECT m FROM Equipos_Movimientos m WHERE m.codigoEquipo = :codigoEquipo ORDER BY m.fechaMovimiento DESC")
    Optional<Equipos_Movimientos> obtenerUltimoMovimiento(@Param("codigoEquipo") String codigoEquipo);

    /**
     * Verifica si el último movimiento de un equipo es de tipo SALIDA (sin entrada posterior).
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
           "FROM Equipos_Movimientos m WHERE m.codigoEquipo = :codigoEquipo " +
           "AND m.fechaMovimiento = (SELECT MAX(m2.fechaMovimiento) FROM Equipos_Movimientos m2 WHERE m2.codigoEquipo = :codigoEquipo) " +
           "AND m.tipoMovimiento = 'SALIDA'")
    boolean tieneSalidaActiva(@Param("codigoEquipo") String codigoEquipo);

    /**
     * Verifica si el último movimiento de un equipo es de tipo ENTRADA (sin salida posterior).
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
           "FROM Equipos_Movimientos m WHERE m.codigoEquipo = :codigoEquipo " +
           "AND m.fechaMovimiento = (SELECT MAX(m2.fechaMovimiento) FROM Equipos_Movimientos m2 WHERE m2.codigoEquipo = :codigoEquipo) " +
           "AND m.tipoMovimiento = 'ENTRADA'")
    boolean tieneEntradaActiva(@Param("codigoEquipo") String codigoEquipo);

    /**
     * Obtiene todos los movimientos de un tipo específico (SALIDA o ENTRADA).
     */
    List<Equipos_Movimientos> findByTipoMovimiento(TipoMovimiento tipoMovimiento);

    /**
     * Obtiene movimientos registrados por un usuario específico.
     */
    List<Equipos_Movimientos> findByRegistradoPor(Integer idUsuario);
}
