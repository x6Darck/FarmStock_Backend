package com.FarmStock_Backend.FarmStock.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FarmStock_Backend.FarmStock.Model.Aprendiz;
import com.FarmStock_Backend.FarmStock.Repository.AprendizRepository;
import com.FarmStock_Backend.FarmStock.Repository.PrestamoRepository;

@Service
public class AprendizLogica {

    private final AprendizRepository aprendizRepository;
    private final PrestamoRepository prestamoRepository;

    public AprendizLogica(AprendizRepository aprendizRepository, PrestamoRepository prestamoRepository) {
        this.aprendizRepository = aprendizRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Crea un aprendiz nuevo validando que no exista el mismo número de documento.
     */
    public Aprendiz crearAprendiz(Aprendiz aprendiz) {
        // Verificar si ya existe un aprendiz con ese número de documento
        Optional<Aprendiz> existente = aprendizRepository.findByNumeroDocumento(aprendiz.getNumeroDocumento());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un aprendiz con el número de documento: " + aprendiz.getNumeroDocumento());
        }
        return aprendizRepository.save(aprendiz);
    }

    
     //Lista todos los aprendices.
     
    public List<Aprendiz> verAprendices() {
        return aprendizRepository.findAll();
    }

    
    //Busca un aprendiz por tipo y número de documento.
     
    public Aprendiz buscarPorDocumento(String tipoDocumento, String numeroDocumento) {
        return aprendizRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró un aprendiz con documento: " + tipoDocumento + " " + numeroDocumento
            ));
    }

    
    //Buscar aprendiz por numero Ficha
    
    public Aprendiz buscarPorNumeroFicha(String numeroFicha) {
        return aprendizRepository.findByNumeroFicha(numeroFicha)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró a ningun aprendiz con este numero de ficha: " + numeroFicha));
    }

    
    //Actualiza los datos básicos del aprendiz indicado por ID.
    
    public Aprendiz actualizarAprendiz(Integer idAprendiz, Aprendiz aprendiz) {
        Optional<Aprendiz> opt = aprendizRepository.findById(idAprendiz);
        if (opt.isPresent()) {
            Aprendiz actual = opt.get();
            actual.setNombre(aprendiz.getNombre());
            actual.setTipoDocumento(aprendiz.getTipoDocumento());
            actual.setNumeroDocumento(aprendiz.getNumeroDocumento());
            actual.setNumeroFicha(aprendiz.getNumeroFicha());
            return aprendizRepository.save(actual);
        } else {
            throw new IllegalArgumentException("No se encontró el aprendiz con id: " + idAprendiz);
        }
    }

    
    //Elimina un aprendiz por ID (y sus préstamos asociados) en una transacción.
    
    @Transactional
    public void eliminarAprendiz(Integer idAprendiz) {
        if (aprendizRepository.existsById(idAprendiz)) {
            // Elimina todos los préstamos asociados a este aprendiz
            prestamoRepository.deleteByAprendizIdAprendiz(idAprendiz);
            // Luego elimina el aprendiz
            aprendizRepository.deleteById(idAprendiz);
        } else {
            throw new IllegalArgumentException("No se encontró el aprendiz con id: " + idAprendiz);
        }
    }
}