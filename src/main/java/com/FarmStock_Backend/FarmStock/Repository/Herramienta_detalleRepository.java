package com.FarmStock_Backend.FarmStock.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;

@Repository
public interface Herramienta_detalleRepository extends JpaRepository<Herramienta_detalle, Integer> {
    List<Herramienta_detalle> findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(Integer idHerramienta);
    Optional<Herramienta_detalle> findByIdDetalle(Integer codigoUnico);
    Optional<Herramienta_detalle> findByCodigoUnico(String CodigoUnico); // <-- SOLO así
}