package com.FarmStock_Backend.FarmStock.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.DTO.MovimientoEquipoDTO;
import com.FarmStock_Backend.FarmStock.Model.Equipos_Computos;
import com.FarmStock_Backend.FarmStock.Model.Equipos_Movimientos;
import com.FarmStock_Backend.FarmStock.Model.Equipos_Movimientos.TipoMovimiento;
import com.FarmStock_Backend.FarmStock.Repository.Equipos_ComputosRepository;
import com.FarmStock_Backend.FarmStock.Repository.Equipos_MovimientosRepository;
import com.FarmStock_Backend.FarmStock.Repository.UsuarioRepository;

@Service
public class EquiposComputosLogica {

    private final Equipos_ComputosRepository equiposComputosRepository;
    private final Equipos_MovimientosRepository equiposMovimientosRepository;
    private final UsuarioRepository usuarioRepository;

    public EquiposComputosLogica(Equipos_ComputosRepository equiposComputosRepository,
                                 Equipos_MovimientosRepository equiposMovimientosRepository,
                                 UsuarioRepository usuarioRepository) {
        this.equiposComputosRepository = equiposComputosRepository;
        this.equiposMovimientosRepository = equiposMovimientosRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Registra un nuevo equipo de cómputo en el sistema.
     * Valida que el código del equipo no exista previamente.
     */
    public Equipos_Computos registrarEquipo(Equipos_Computos equipo) {
        // Validar que el código no exista
        if (equiposComputosRepository.existsByCodigoEquipo(equipo.getCodigoEquipo())) {
            throw new IllegalArgumentException(
                "Ya existe un equipo con el código: " + equipo.getCodigoEquipo());
        }

        // Establecer fecha de registro si no viene
        if (equipo.getFechaRegistro() == null) {
            equipo.setFechaRegistro(LocalDateTime.now());
        }

        return equiposComputosRepository.save(equipo);
    }

    /**
     * Registra una SALIDA de equipo del SENA.
     * Valida que la cédula ingresada coincida con la cédula registrada del equipo.
     * Valida que tenga una entrada activa previa.
     */
    public Equipos_Movimientos registrarSalida(String codigoEquipo, String cedulaIngresada, String observacion) {
        // Validar que el equipo existe y obtener su información
        Equipos_Computos equipo = equiposComputosRepository.findByCodigoEquipo(codigoEquipo)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe un equipo con el código: " + codigoEquipo));

        // Validar que la cédula ingresada coincida con la del equipo
        if (!equipo.getCedula().equals(cedulaIngresada)) {
            throw new IllegalArgumentException(
                "La cédula ingresada (" + cedulaIngresada + ") no coincide con la cédula " +
                "registrada para el equipo " + codigoEquipo + " (" + equipo.getCedula() + ")");
        }

        // Validar que tenga entrada activa (sin salida)
        if (!equiposMovimientosRepository.tieneEntradaActiva(codigoEquipo)) {
            throw new IllegalArgumentException(
                "El equipo " + codigoEquipo + " no tiene una entrada activa. " +
                "Debe registrarse su entrada antes de poder registrar una salida.");
        }

        // Crear movimiento de salida usando el ID del equipo como registrador
        Equipos_Movimientos movimiento = new Equipos_Movimientos(
            codigoEquipo,
            TipoMovimiento.SALIDA,
            LocalDateTime.now(),
            equipo.getIdEquipo(),
            observacion
        );

        return equiposMovimientosRepository.save(movimiento);
    }

    /**
     * Registra una ENTRADA de equipo al SENA.
     * Valida que la cédula ingresada coincida con la cédula registrada del equipo.
     * Valida que no tenga una entrada activa (sin salida).
     */
    public Equipos_Movimientos registrarEntrada(String codigoEquipo, String cedulaIngresada, String observacion) {
        // Validar que el equipo existe y obtener su información
        Equipos_Computos equipo = equiposComputosRepository.findByCodigoEquipo(codigoEquipo)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe un equipo con el código: " + codigoEquipo));

        // Validar que la cédula ingresada coincida con la del equipo
        if (!equipo.getCedula().equals(cedulaIngresada)) {
            throw new IllegalArgumentException(
                "La cédula ingresada (" + cedulaIngresada + ") no coincide con la cédula " +
                "registrada para el equipo " + codigoEquipo + " (" + equipo.getCedula() + ")");
        }

        // Validar que no tenga entrada activa (sin salida)
        if (equiposMovimientosRepository.tieneEntradaActiva(codigoEquipo)) {
            throw new IllegalArgumentException(
                "El equipo " + codigoEquipo + " ya tiene una entrada activa. " +
                "Debe registrarse su salida antes de una nueva entrada.");
        }

        // Crear movimiento de entrada usando el ID del equipo como registrador
        Equipos_Movimientos movimiento = new Equipos_Movimientos(
            codigoEquipo,
            TipoMovimiento.ENTRADA,
            LocalDateTime.now(),
            equipo.getIdEquipo(),
            observacion
        );

        return equiposMovimientosRepository.save(movimiento);
    }

    /**
     * Obtiene todos los equipos registrados.
     */
    public List<Equipos_Computos> obtenerTodosLosEquipos() {
        return equiposComputosRepository.findAll();
    }

    /**
     * Busca un equipo por su código.
     */
    public Equipos_Computos buscarPorCodigo(String codigoEquipo) {
        return equiposComputosRepository.findByCodigoEquipo(codigoEquipo)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró equipo con código: " + codigoEquipo));
    }

    /**
     * Busca equipos asignados a una persona por cédula.
     */
    public List<Equipos_Computos> buscarPorCedula(String cedula) {
        return equiposComputosRepository.findByCedula(cedula);
    }

    /**
     * Obtiene el historial completo de movimientos de un equipo específico.
     * Devuelve DTOs con la cédula y nombre de la persona desde equipos_computos.
     */
    public List<MovimientoEquipoDTO> obtenerHistorialMovimientos(String codigoEquipo) {
        List<Equipos_Movimientos> movimientos = equiposMovimientosRepository
            .findByCodigoEquipoOrderByFechaMovimientoDesc(codigoEquipo);
        
        // Obtener información del equipo una sola vez
        Equipos_Computos equipo = equiposComputosRepository.findByCodigoEquipo(codigoEquipo)
            .orElse(null);
        
        String cedula = equipo != null ? equipo.getCedula() : "Desconocido";
        String nombrePersona = equipo != null ? equipo.getNombrePersona() : "Desconocido";
        
        // Convertir cada movimiento a DTO con información del equipo
        return movimientos.stream()
            .map(mov -> new MovimientoEquipoDTO(
                mov.getIdMovimiento().longValue(),
                mov.getCodigoEquipo(),
                mov.getTipoMovimiento(),
                mov.getFechaMovimiento(),
                cedula,
                nombrePersona,
                mov.getObservacion()
            ))
            .collect(Collectors.toList());
    }   

    /**
     * Obtiene todos los movimientos del sistema.
     */

    /**
     * Obtiene todos los movimientos del sistema como DTO con cédula y nombre.
     */
    public List<MovimientoEquipoDTO> obtenerTodosLosMovimientos() {
        List<Equipos_Movimientos> movimientos = equiposMovimientosRepository.findAll();
        return movimientos.stream().map(mov -> {
            Equipos_Computos equipo = equiposComputosRepository.findByCodigoEquipo(mov.getCodigoEquipo()).orElse(null);
            String cedula = equipo != null ? equipo.getCedula() : "Desconocido";
            String nombrePersona = equipo != null ? equipo.getNombrePersona() : "Desconocido";
            return new MovimientoEquipoDTO(
                mov.getIdMovimiento().longValue(),
                mov.getCodigoEquipo(),
                mov.getTipoMovimiento(),
                mov.getFechaMovimiento(),
                cedula,
                nombrePersona,
                mov.getObservacion()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Obtiene todos los movimientos de tipo SALIDA.
     */

    /**
     * Obtiene todos los movimientos de tipo SALIDA como DTO con cédula y nombre.
     */
    public List<MovimientoEquipoDTO> obtenerSalidas() {
        List<Equipos_Movimientos> movimientos = equiposMovimientosRepository.findByTipoMovimiento(TipoMovimiento.SALIDA);
        return movimientos.stream().map(mov -> {
            Equipos_Computos equipo = equiposComputosRepository.findByCodigoEquipo(mov.getCodigoEquipo()).orElse(null);
            String cedula = equipo != null ? equipo.getCedula() : "Desconocido";
            String nombrePersona = equipo != null ? equipo.getNombrePersona() : "Desconocido";
            return new MovimientoEquipoDTO(
                mov.getIdMovimiento().longValue(),
                mov.getCodigoEquipo(),
                mov.getTipoMovimiento(),
                mov.getFechaMovimiento(),
                cedula,
                nombrePersona,
                mov.getObservacion()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Obtiene todos los movimientos de tipo ENTRADA.
     */

    /**
     * Obtiene todos los movimientos de tipo ENTRADA como DTO con cédula y nombre.
     */
    public List<MovimientoEquipoDTO> obtenerEntradas() {
        List<Equipos_Movimientos> movimientos = equiposMovimientosRepository.findByTipoMovimiento(TipoMovimiento.ENTRADA);
        return movimientos.stream().map(mov -> {
            Equipos_Computos equipo = equiposComputosRepository.findByCodigoEquipo(mov.getCodigoEquipo()).orElse(null);
            String cedula = equipo != null ? equipo.getCedula() : "Desconocido";
            String nombrePersona = equipo != null ? equipo.getNombrePersona() : "Desconocido";
            return new MovimientoEquipoDTO(
                mov.getIdMovimiento().longValue(),
                mov.getCodigoEquipo(),
                mov.getTipoMovimiento(),
                mov.getFechaMovimiento(),
                cedula,
                nombrePersona,
                mov.getObservacion()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un equipo.
     */
    public Equipos_Computos actualizarEquipo(Integer idEquipo, Equipos_Computos equipoActualizado) {
        Equipos_Computos equipoExistente = equiposComputosRepository.findById(idEquipo)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró equipo con ID: " + idEquipo));

        // Actualizar campos editables
        if (equipoActualizado.getNombrePersona() != null) {
            equipoExistente.setNombrePersona(equipoActualizado.getNombrePersona());
        }
        if (equipoActualizado.getCedula() != null) {
            equipoExistente.setCedula(equipoActualizado.getCedula());
        }
        if (equipoActualizado.getNombreEquipo() != null) {
            equipoExistente.setNombreEquipo(equipoActualizado.getNombreEquipo());
        }

        return equiposComputosRepository.save(equipoExistente);
    }

    /**
     * Elimina un equipo del sistema.
     */
    public void eliminarEquipo(Integer idEquipo) {
        if (!equiposComputosRepository.existsById(idEquipo)) {
            throw new IllegalArgumentException("No existe equipo con ID: " + idEquipo);
        }
        equiposComputosRepository.deleteById(idEquipo);
    }
}
