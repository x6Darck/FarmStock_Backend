package com.FarmStock_Backend.FarmStock.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.FarmStock_Backend.FarmStock.Model.Equipos_Computos;

@Repository
public interface Equipos_ComputosRepository extends JpaRepository<Equipos_Computos, Integer> {

    /**
     * Busca un equipo por su código único.
     */
    Optional<Equipos_Computos> findByCodigoEquipo(String codigoEquipo);

    /**
     * Busca todos los equipos asignados a una persona por cédula.
     */
    List<Equipos_Computos> findByCedula(String cedula);

    /**
     * Busca equipos por nombre de persona (búsqueda parcial, case-insensitive).
     */
    @Query("SELECT e FROM Equipos_Computos e WHERE LOWER(e.nombrePersona) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Equipos_Computos> buscarPorNombrePersona(@Param("nombre") String nombre);

    /**
     * Verifica si existe un equipo con el código dado.
     */
    boolean existsByCodigoEquipo(String codigoEquipo);
}
