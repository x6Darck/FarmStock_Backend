package com.FarmStock_Backend.FarmStock.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FarmStock_Backend.FarmStock.Model.Herramientas;

public interface HerramientasRepository extends JpaRepository<Herramientas, Integer> {
    Optional<Herramientas> findByNombre(String nombre);
    Optional<Herramientas> findByIdHerramienta(Integer id);
    List<Herramientas> findByFechaRegistro(LocalDate fecha);
}