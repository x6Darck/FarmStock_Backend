package com.FarmStock_Backend.FarmStock.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FarmStock_Backend.FarmStock.Model.Prestamo;
import com.FarmStock_Backend.FarmStock.Service.PrestamoLogica;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoLogica prestamoLogica;

    @Autowired
    public PrestamoController(PrestamoLogica prestamoLogica) {
        this.prestamoLogica = prestamoLogica;
    }

    /**
     * POST /prestamos/crear
     * Registra un nuevo préstamo de herramienta a un aprendiz
     * Requiere: codigoUnico (herramienta), idUsuario (quien presta), numeroDocumento (aprendiz)
     */
    @PostMapping("/crear")
    public ResponseEntity<Prestamo> crearPrestamo(@RequestParam String codigoUnico,@RequestParam Integer idUsuario, @RequestParam String numeroDocumento, @RequestBody Prestamo prestamo) {
        Prestamo creado = prestamoLogica.crearPrestamo(codigoUnico, idUsuario, numeroDocumento,  prestamo);
        
        return ResponseEntity.ok(creado);
    }
    /**
     * PUT /prestamos/devolver/codigo/{codigoUnico}
     * Procesa la devolución de una herramienta prestada
     * Registra fecha/hora de devolución y cambia estado a "Finalizado"
     */
    @PutMapping("/devolver/codigo/{codigoUnico}")
    public ResponseEntity<?> devolverPorCodigo(@PathVariable String codigoUnico) {
        try {
            Prestamo prestamoActualizado = prestamoLogica.aceptarDevolucionPorCodigo(codigoUnico);

            return ResponseEntity.ok(prestamoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     * GET /prestamos/todos
     * Lista todos los préstamos del sistema (activos, finalizados, vencidos)
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Prestamo>> listarTodosLosPrestamos() {
        return ResponseEntity.ok(prestamoLogica.obtenerTodosLosPrestamos());
    }

    /**
     * GET /prestamos/activos
     * Lista solo los préstamos activos (sin devolver)
     * Usado para mostrar la tabla "Registro Salida" en el frontend
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivos() {
        return ResponseEntity.ok(prestamoLogica.obtenerTodosLosPrestamosActivos());
    }

    /**
     * GET /prestamos/devueltos
     * Lista solo los préstamos ya devueltos (finalizados)
     * Usado para mostrar el historial de devoluciones
     */
    @GetMapping("/devueltos")
    public ResponseEntity<List<Prestamo>> listarPrestamosDevueltos() {
        return ResponseEntity.ok(prestamoLogica.obtenerTodosLosPrestamosDevueltos());
    }

    /**
     * GET /prestamos/activo/codigo/{codigoUnico}
     * Busca un préstamo activo específico por código de herramienta
     * Retorna error si no hay préstamo activo con ese código
     */
    @GetMapping("/activo/codigo/{codigoUnico}")
    public ResponseEntity<?> obtenerPrestamoActivoPorCodigo(@PathVariable String codigoUnico) {
        try {
            Prestamo prestamo = prestamoLogica.obtenerPrestamoActivo(codigoUnico);
            return ResponseEntity.ok(prestamo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * GET /prestamos/codigo/{codigoUnico}
     * Busca un préstamo activo por código único de herramienta
     * Solo retorna si el préstamo está activo (sin devolver)
     */
    @GetMapping("/codigo/{codigoUnico}")
    public ResponseEntity<?> obtenerPrestamoPorCodigo(@PathVariable String codigoUnico) {
        Optional<Prestamo> prestamo = prestamoLogica.obtenerPrestamoPorCodigoUnico(codigoUnico);
        if (prestamo.isPresent()) {
            return ResponseEntity.ok(prestamo.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No hay préstamo activo para el código: " + codigoUnico);
        }
    }

    /**
     * PUT /prestamos/{id}
     * Actualiza los datos de un préstamo existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(@PathVariable Integer id, @RequestBody Prestamo prestamoActualizado) {
        Prestamo actualizado = prestamoLogica.actualizarPrestamo(id, prestamoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * DELETE /prestamos/{id}
     * Elimina un préstamo del sistema por su ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Integer id) {
        prestamoLogica.eliminarPrestamo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /prestamos/usuario/{idUsuario}
     * Lista todos los préstamos realizados por un usuario específico
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosPorUsuario(@PathVariable Integer idUsuario) {
        List<Prestamo> prestamos = prestamoLogica.obtenerPrestamosPorUsuario(idUsuario);
        return ResponseEntity.ok(prestamos);
    }
}