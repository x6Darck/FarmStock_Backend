package com.FarmStock_Backend.FarmStock.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FarmStock_Backend.FarmStock.Model.Aprendiz;

public interface AprendizRepository extends JpaRepository<Aprendiz, Integer> {

    Optional<Aprendiz> findByNumeroDocumento(String numerodocumento);

    Optional<Aprendiz> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
    Optional<Aprendiz> findByNumeroFicha(String numeroFicha);
    

}
