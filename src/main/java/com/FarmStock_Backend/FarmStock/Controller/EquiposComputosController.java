package com.FarmStock_Backend.FarmStock.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FarmStock_Backend.FarmStock.DTO.MovimientoEquipoDTO;
import com.FarmStock_Backend.FarmStock.Model.Equipos_Computos;
import com.FarmStock_Backend.FarmStock.Model.Equipos_Movimientos;
import com.FarmStock_Backend.FarmStock.Service.EquiposComputosLogica;

import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar equipos de cómputo y sus movimientos (salidas/entradas).
 */
@RestController
@RequestMapping("/equipos-computos")
@CrossOrigin(origins = "*")
public class EquiposComputosController {

    private final EquiposComputosLogica equiposLogica;

    public EquiposComputosController(EquiposComputosLogica equiposLogica) {
        this.equiposLogica = equiposLogica;
    }

    /**
     * POST /equipos-computos
     * Registra un nuevo equipo de cómputo en el sistema.
     * Body: { "nombre_persona", "cedula", "nombre_equipo", "codigo_equipo", "fecha_registro" }
     */
    @PostMapping
    public ResponseEntity<?> registrarEquipo(@Valid @RequestBody Equipos_Computos equipo) {
        try {
            Equipos_Computos equipoCreado = equiposLogica.registrarEquipo(equipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(equipoCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * POST /equipos-computos/salida?codigoEquipo=X&idUsuario=Y&observacion=Z
     * Registra una SALIDA de equipo.
     * Valida que la cédula (idUsuario) coincida con la registrada en el equipo.
     */
    @PostMapping("/salida")
    public ResponseEntity<?> registrarSalida(
            @RequestParam String codigoEquipo,
            @RequestParam String idUsuario,
            @RequestParam(required = false) String observacion) {
        try {
            Equipos_Movimientos movimiento = equiposLogica.registrarSalida(codigoEquipo, idUsuario, observacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * POST /equipos-computos/entrada?codigoEquipo=X&idUsuario=Y&observacion=Z
     * Registra una ENTRADA de equipo.
     * Valida que la cédula (idUsuario) coincida con la registrada en el equipo.
     */
    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(
            @RequestParam String codigoEquipo,
            @RequestParam String idUsuario,
            @RequestParam(required = false) String observacion) {
        try {
            Equipos_Movimientos movimiento = equiposLogica.registrarEntrada(codigoEquipo, idUsuario, observacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * GET /equipos-computos
     * Lista todos los equipos registrados.
     */
    @GetMapping
    public ResponseEntity<List<Equipos_Computos>> listarTodosLosEquipos() {
        return ResponseEntity.ok(equiposLogica.obtenerTodosLosEquipos());
    }

    /**
     * GET /equipos-computos/codigo/{codigoEquipo}
     * Busca un equipo por su código único.
     */
    @GetMapping("/codigo/{codigoEquipo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigoEquipo) {
        try {
            Equipos_Computos equipo = equiposLogica.buscarPorCodigo(codigoEquipo);
            return ResponseEntity.ok(equipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * GET /equipos-computos/cedula/{cedula}
     * Lista equipos asignados a una persona por cédula.
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<List<Equipos_Computos>> buscarPorCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(equiposLogica.buscarPorCedula(cedula));
    }

    /**
     * GET /equipos-computos/historial/{codigoEquipo}
     * Obtiene el historial completo de movimientos de un equipo con información del usuario (cédula y nombre).
     */
    @GetMapping("/historial/{codigoEquipo}")
    public ResponseEntity<List<MovimientoEquipoDTO>> obtenerHistorial(@PathVariable String codigoEquipo) {
        return ResponseEntity.ok(equiposLogica.obtenerHistorialMovimientos(codigoEquipo));
    }

    /**
     * GET /equipos-computos/movimientos
     * Lista todos los movimientos del sistema.
     */

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoEquipoDTO>> listarTodosLosMovimientos() {
        return ResponseEntity.ok(equiposLogica.obtenerTodosLosMovimientos());
    }

    /**
     * GET /equipos-computos/movimientos/salidas
     * Lista solo los movimientos de tipo SALIDA.
     */

    @GetMapping("/movimientos/salidas")
    public ResponseEntity<List<MovimientoEquipoDTO>> listarSalidas() {
        return ResponseEntity.ok(equiposLogica.obtenerSalidas());
    }

    /**
     * GET /equipos-computos/movimientos/entradas
     * Lista solo los movimientos de tipo ENTRADA.
     */

    @GetMapping("/movimientos/entradas")
    public ResponseEntity<List<MovimientoEquipoDTO>> listarEntradas() {
        return ResponseEntity.ok(equiposLogica.obtenerEntradas());
    }

    /**
     * PUT /equipos-computos/{id}
     * Actualiza los datos de un equipo existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEquipo(
            @PathVariable Integer id,
            @RequestBody Equipos_Computos equipoActualizado) {
        try {
            Equipos_Computos equipo = equiposLogica.actualizarEquipo(id, equipoActualizado);
            return ResponseEntity.ok(equipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * DELETE /equipos-computos/{id}
     * Elimina un equipo del sistema.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEquipo(@PathVariable Integer id) {
        try {
            equiposLogica.eliminarEquipo(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
