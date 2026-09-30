package com.FarmStock_Backend.FarmStock.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FarmStock_Backend.FarmStock.Model.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    
    List<Prestamo> findByUsuario_IdUsuario(Integer idUsuario);

    Optional<Prestamo> findByHerramientaDetalle_CodigoUnicoAndFechaDevolucionIsNull(String codigoUnico);

    List<Prestamo> findByEstado(String estado);
    
    void deleteByAprendizIdAprendiz(Integer idAprendiz);
}
