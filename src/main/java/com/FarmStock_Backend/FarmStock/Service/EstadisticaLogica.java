package com.FarmStock_Backend.FarmStock.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.DTO.HerramientaEstadisticaDTO;
import com.FarmStock_Backend.FarmStock.Model.Herramientas;
import com.FarmStock_Backend.FarmStock.Repository.HerramientasRepository;
import com.FarmStock_Backend.FarmStock.Repository.MantenimientoRepository;

@Service
public class EstadisticaLogica {

    private final HerramientasRepository herramientasRepository;
    private final MantenimientoRepository mantenimientoRepository;

    public EstadisticaLogica(HerramientasRepository herramientasRepository,
                             MantenimientoRepository mantenimientoRepository) {
        this.herramientasRepository = herramientasRepository;
        this.mantenimientoRepository = mantenimientoRepository;
    }

    /**
     * Genera estadísticas de todas las herramientas.
     * Retorna: id, nombre, cantidad de préstamos, daños y mantenimientos.
     */
    public List<HerramientaEstadisticaDTO> obtenerEstadisticasHerramientas() {
        List<Herramientas> herramientas = herramientasRepository.findAll();

        return herramientas.stream().map(h -> {
            Long prestamos = Long.valueOf(h.getContadorPrestamos() != null ? h.getContadorPrestamos() : 0);
            Long danos = mantenimientoRepository.contarPorHerramientaYTipo(h.getIdHerramienta(), "DAÑO");
            Long mantenimientos = mantenimientoRepository.contarPorHerramientaYTipo(h.getIdHerramienta(), "MANTENIMIENTO");

            return new HerramientaEstadisticaDTO(
                h.getIdHerramienta(),
                h.getNombre(),
                prestamos,
                danos,
                mantenimientos
            );
        }).collect(Collectors.toList());
    }

    /**
     * Obtiene estadísticas de una herramienta específica.
     */
    public HerramientaEstadisticaDTO obtenerEstadisticaPorHerramienta(Integer idHerramienta) {
        Herramientas h = herramientasRepository.findById(idHerramienta)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la herramienta con id: " + idHerramienta));

        Long prestamos = Long.valueOf(h.getContadorPrestamos() != null ? h.getContadorPrestamos() : 0);
        Long danos = mantenimientoRepository.contarPorHerramientaYTipo(h.getIdHerramienta(), "DAÑO");
        Long mantenimientos = mantenimientoRepository.contarPorHerramientaYTipo(h.getIdHerramienta(), "MANTENIMIENTO");

        return new HerramientaEstadisticaDTO(
            h.getIdHerramienta(),
            h.getNombre(),
            prestamos,
            danos,
            mantenimientos
        );
    }
}
