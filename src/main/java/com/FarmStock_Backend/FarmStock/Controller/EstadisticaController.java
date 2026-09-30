package com.FarmStock_Backend.FarmStock.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FarmStock_Backend.FarmStock.DTO.HerramientaEstadisticaDTO;
import com.FarmStock_Backend.FarmStock.Service.EstadisticaLogica;

/**
 * Controlador REST para obtener estadísticas de herramientas.
 */
@RestController
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final EstadisticaLogica estadisticaLogica;

    public EstadisticaController(EstadisticaLogica estadisticaLogica) {
        this.estadisticaLogica = estadisticaLogica;
    }

    /**
     * GET /estadisticas/herramientas
     * Obtiene estadísticas de todas las herramientas.
     * Retorna: id, nombre, cantidad de préstamos, daños y mantenimientos.
     */
    @GetMapping("/herramientas")
    public ResponseEntity<List<HerramientaEstadisticaDTO>> obtenerEstadisticasHerramientas() {
        return ResponseEntity.ok(estadisticaLogica.obtenerEstadisticasHerramientas());
    }

    /**
     * GET /estadisticas/herramientas/{id}
     * Obtiene estadísticas de una herramienta específica.
     */
    @GetMapping("/herramientas/{id}")
    public ResponseEntity<?> obtenerEstadisticaPorHerramienta(@PathVariable Integer id) {
        try {
            HerramientaEstadisticaDTO estadistica = estadisticaLogica.obtenerEstadisticaPorHerramienta(id);
            return ResponseEntity.ok(estadistica);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
