package com.FarmStock_Backend.FarmStock.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;
import com.FarmStock_Backend.FarmStock.Service.HerramientaDetalleLogica;
//lka haga drop datanbase
@RestController
@RequestMapping("/api/herramienta-detalle")
@CrossOrigin(origins = "*")
public class Herramienta_DetalleController {
     private final HerramientaDetalleLogica herramientaDetalleLogica;

    public Herramienta_DetalleController(HerramientaDetalleLogica herramientaDetalleLogica) {
        this.herramientaDetalleLogica = herramientaDetalleLogica;
    }

    /**
     * GET /api/herramienta-detalle
     * Obtiene todos los detalles de herramientas
     */
    @GetMapping
    public List<Herramienta_detalle> obtenerTodosLosDetalles() {
        return herramientaDetalleLogica.obtenerTodos();
    }

    /**
     * GET /api/herramienta-detalle/herramienta/{idHerramienta}
     * Obtiene todos los detalles de una herramienta específica
     */
    @GetMapping("/herramienta/{idHerramienta}")
    public List<Herramienta_detalle> obtenerPorHerramienta(@PathVariable Integer idHerramienta) {
        return herramientaDetalleLogica.obtenerHerramientas(idHerramienta);
    }

    /**
     * GET /api/herramienta-detalle/codigo/{codigoUnico}
     * Obtiene un detalle específico por código único
     */
    @GetMapping("/codigo/{codigoUnico}")
    public ResponseEntity<?> obtenerPorCodigoUnico(@PathVariable String codigoUnico) {
        try {
            Herramienta_detalle detalle = herramientaDetalleLogica.obtenerPorCodigoUnico(codigoUnico);
            return ResponseEntity.ok(detalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{idDetalle}")
    public Herramienta_detalle actualizarDetalle(
            @PathVariable Integer idDetalle,
            @RequestBody Herramienta_detalle detalleActualizado) {
        return herramientaDetalleLogica.actualizarHerramientaDetalle(idDetalle, detalleActualizado);
    }

    /**
     * DELETE /api/herramienta-detalle/{idDetalle}
     * Elimina una unidad física específica y actualiza la cantidad en la herramienta general
     */
    @DeleteMapping("/{idDetalle}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Integer idDetalle) {
        try {
            herramientaDetalleLogica.eliminarHerramientaDetalle(idDetalle);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * GET /api/herramienta-detalle/qr/{codigoUnico}
     * Obtiene la imagen QR de una herramienta para imprimir
     * Retorna la imagen PNG directamente
     */
    @GetMapping("/qr/{codigoUnico}")
    public ResponseEntity<?> obtenerCodigoQR(@PathVariable String codigoUnico) {
        try {
            File archivoQR = herramientaDetalleLogica.obtenerImagenQR(codigoUnico);
            byte[] imagenBytes = Files.readAllBytes(archivoQR.toPath());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagenBytes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al leer el archivo QR: " + e.getMessage());
        }
    }
}