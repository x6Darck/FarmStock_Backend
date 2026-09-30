package com.FarmStock_Backend.FarmStock.Controller;

import java.util.List;

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

import com.FarmStock_Backend.FarmStock.Model.Aprendiz;
import com.FarmStock_Backend.FarmStock.Service.AprendizLogica;

import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar aprendices.
 * Expone endpoints para operaciones CRUD sobre aprendices.
 */
@RestController
@RequestMapping("/aprendices")
public class AprendizController {

    private final AprendizLogica aprendizLogica;

    public AprendizController(AprendizLogica aprendizLogica) {
        this.aprendizLogica = aprendizLogica;
    }

    
    @PostMapping
    public ResponseEntity<?> crearAprendiz(@Valid @RequestBody Aprendiz aprendiz) {
        try {
            Aprendiz creado = aprendizLogica.crearAprendiz(aprendiz);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<Aprendiz>> listarAprendices() {
        return ResponseEntity.ok(aprendizLogica.verAprendices());
    }



    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorDocumento(
            @RequestParam String tipoDocumento,
            @RequestParam String numeroDocumento) {
        try {
            Aprendiz aprendiz = aprendizLogica.buscarPorDocumento(tipoDocumento, numeroDocumento);
            return ResponseEntity.ok(aprendiz);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAprendiz(
            @PathVariable Integer id,
            @Valid @RequestBody Aprendiz aprendiz) {
        try {
            Aprendiz actualizado = aprendizLogica.actualizarAprendiz(id, aprendiz);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAprendiz(@PathVariable Integer id) {
        try {
            aprendizLogica.eliminarAprendiz(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
