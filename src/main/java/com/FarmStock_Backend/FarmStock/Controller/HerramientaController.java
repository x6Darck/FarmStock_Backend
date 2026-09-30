package com.FarmStock_Backend.FarmStock.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FarmStock_Backend.FarmStock.Model.Herramientas;
import com.FarmStock_Backend.FarmStock.Service.HerramientaLogica;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/herramienta")
public class HerramientaController {
    private final HerramientaLogica service;

    public HerramientaController(HerramientaLogica service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Herramientas> crearHerramienta(@Valid @RequestBody Herramientas herramienta){
        Herramientas nuevaHerramienta = service.crearHerramienta(herramienta);
        return ResponseEntity.status(201).body(nuevaHerramienta);
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<Herramientas>> obtenerHerramientasDeHoy() {
        return ResponseEntity.ok(service.obtenerHerramientasDeHoy());
    }

    @GetMapping
    public ResponseEntity<List<Herramientas>> obtenerTodasHerramientas() {
        return ResponseEntity.ok(service.obtenerTodasHerramientas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Herramientas> obtenerPorId(@PathVariable Integer id) {
        Herramientas h = service.obtenerPorId(id);
        return ResponseEntity.ok(h);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Herramientas> actualizarHerramienta(@PathVariable Integer id,
                                                              @Valid @RequestBody Herramientas herramienta) {
        Herramientas actualizada = service.actualizarHerramienta(id, herramienta);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHerramienta(@PathVariable Integer id) {
        service.eliminarHerramienta(id);
        return ResponseEntity.noContent().build();
    }
}