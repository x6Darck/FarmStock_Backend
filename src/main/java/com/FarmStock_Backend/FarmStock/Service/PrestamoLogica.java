package com.FarmStock_Backend.FarmStock.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.Model.Aprendiz;
import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;
import com.FarmStock_Backend.FarmStock.Model.Herramientas;
import com.FarmStock_Backend.FarmStock.Model.Prestamo;
import com.FarmStock_Backend.FarmStock.Model.Usuario;
import com.FarmStock_Backend.FarmStock.Repository.AprendizRepository;
import com.FarmStock_Backend.FarmStock.Repository.Herramienta_detalleRepository;
import com.FarmStock_Backend.FarmStock.Repository.HerramientasRepository;
import com.FarmStock_Backend.FarmStock.Repository.PrestamoRepository;
import com.FarmStock_Backend.FarmStock.Repository.UsuarioRepository;

@Service
public class PrestamoLogica {

    private final PrestamoRepository prestamoRepository;
    private final Herramienta_detalleRepository herramientadetalleRepository;   
    private final UsuarioRepository usuarioRepository;
    private final AprendizRepository aprendizRepository;
    private final HerramientasRepository herramientasRepository;

    @Autowired
    public PrestamoLogica(PrestamoRepository prestamoRepository, Herramienta_detalleRepository herramientadetalleRepository, UsuarioRepository usuarioRepository, AprendizRepository aprendizRepository, HerramientasRepository herramientasRepository) {
        this.prestamoRepository = prestamoRepository;
        this.herramientadetalleRepository = herramientadetalleRepository;
        this.usuarioRepository = usuarioRepository;
        this.aprendizRepository = aprendizRepository;
        this.herramientasRepository = herramientasRepository;
    }

    /**
     * Crea un nuevo préstamo de herramienta
     * Busca la herramienta por código único, el usuario por ID y el aprendiz por número de documento
     * Asocia todos estos datos al préstamo y lo guarda en la base de datos
     * Incrementa el contador de préstamos de la herramienta general Y del detalle individual
     * Valida que la herramienta no tenga un préstamo activo antes de crear uno nuevo
     */
    public Prestamo crearPrestamo(String codigo, Integer idUsuario, String numeroDocumento, Prestamo prestamo) {
        // Normalizar el código: reemplazar barras (/) por guiones (-) para coincidir con el formato de la BD
        String codigoNormalizado = codigo.replace("/", "-");
        
        Herramienta_detalle herramientaDetalle = herramientadetalleRepository.findByCodigoUnico(codigoNormalizado)
            .orElseThrow(() -> new RuntimeException("no se encontro ninguna herramienta con este codigo: " + codigoNormalizado));
        
        // ⚠️ VALIDACIÓN: Verificar que no tenga préstamo activo
        Optional<Prestamo> prestamoActivo = prestamoRepository
            .findByHerramientaDetalle_CodigoUnicoAndFechaDevolucionIsNull(codigoNormalizado);
        
        if (prestamoActivo.isPresent()) {
            throw new RuntimeException(
                "La herramienta " + codigoNormalizado + " ya tiene un préstamo activo. " +
                "Debe ser devuelta antes de prestarla nuevamente.");
        }
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("no se encontro usuario con este id: " + idUsuario));
        Herramientas herramienta = herramientaDetalle.getHerramienta();

        Aprendiz aprendiz = aprendizRepository.findByNumeroDocumento(numeroDocumento).orElseThrow(() -> new RuntimeException("No se encontro ningun Aprendiz con este Documento" + numeroDocumento));

        prestamo.setHerramientaDetalle(herramientaDetalle);
        prestamo.setHerramienta(herramienta);
        prestamo.setUsuario(usuario);
        prestamo.setAprendiz(aprendiz); 

        // Incrementar contador de préstamos en la herramienta general (para estadísticas agregadas)
        Integer contadorGeneral = herramienta.getContadorPrestamos();
        herramienta.setContadorPrestamos(contadorGeneral != null ? contadorGeneral + 1 : 1);
        herramientasRepository.save(herramienta);

        // Incrementar contador de préstamos en el detalle individual (para rastrear unidades específicas)
        Integer contadorDetalle = herramientaDetalle.getContadorPrestamos();
        herramientaDetalle.setContadorPrestamos(contadorDetalle != null ? contadorDetalle + 1 : 1);
        herramientadetalleRepository.save(herramientaDetalle);

        return prestamoRepository.save(prestamo);
    }

    /**
     * Procesa la devolución de una herramienta
     * Busca el préstamo activo por código único, registra la fecha de devolución
     * y cambia el estado del préstamo a "Finalizado"
     */
    public Prestamo aceptarDevolucionPorCodigo(String codigo) {
        try {
            // Normalizar el código: reemplazar barras (/) por guiones (-)
            String codigoNormalizado = codigo.replace("/", "-");
            
            Prestamo prestamo = prestamoRepository
                .findByHerramientaDetalle_CodigoUnicoAndFechaDevolucionIsNull(codigoNormalizado)
                .orElseThrow(() -> new RuntimeException("No hay préstamo activo para ese código: " + codigoNormalizado));

            prestamo.setFechaDevolucion(LocalDateTime.now());
            prestamo.setEstado("Finalizado");

            Prestamo saved = prestamoRepository.save(prestamo);
            return saved;
        } catch (Exception ex) {
            // log completo para ver la causa real
            ex.printStackTrace();
            // vuelve a lanzar para que el controlador lo capture o devolvemos un runtime con mensaje más claro
            throw new RuntimeException("Error al devolver préstamo: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
    }
    /**
     * Obtiene un préstamo activo específico por código único
     * Verifica que el préstamo exista, no tenga fecha de devolución y esté en estado "Activo"
     */
    public Prestamo obtenerPrestamoActivo(String codigo) {
        // Normalizar el código: reemplazar barras (/) por guiones (-)
        String codigoNormalizado = codigo.replace("/", "-");
        
        Prestamo prestamo = prestamoRepository
                .findByHerramientaDetalle_CodigoUnicoAndFechaDevolucionIsNull(codigoNormalizado)
                .orElseThrow(() -> new RuntimeException("No hay préstamo activo para ese código: " + codigoNormalizado));
        
        if (!prestamo.getEstado().equals("Activo")) {
            throw new RuntimeException("La herramienta ya se devolvió o no está en préstamo activo");
        }
        
        return prestamo;
    }
    
    /**
     * Lista todos los préstamos activos del sistema
     * Filtra los préstamos que tienen estado "Activo" y no tienen fecha de devolución
     * Usado para mostrar la tabla de "Registro Salida" en el frontend
     */
    public List<Prestamo> obtenerTodosLosPrestamosActivos() {
        return prestamoRepository.findAll().stream()
                .filter(p -> p.getEstado().equals("Activo") && p.getFechaDevolucion() == null)
                .toList();
    }

    /**
     * Lista todos los préstamos ya devueltos (finalizados)
     * Filtra los préstamos que tienen fecha de devolución registrada
     * Usado para mostrar el historial de devoluciones
     */
    public List<Prestamo> obtenerTodosLosPrestamosDevueltos() {
        return prestamoRepository.findAll().stream()
                .filter(p -> p.getFechaDevolucion() != null)
                .toList();
    }








    
    /**
     * Obtiene todos los préstamos registrados en el sistema
     * Incluye préstamos activos, finalizados y vencidos
     */
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoRepository.findAll(); 
    }

    /**
     * Busca un préstamo activo por el código único de la herramienta
     * Solo retorna el préstamo si está activo (sin fecha de devolución)
     */
    public Optional<Prestamo> obtenerPrestamoPorCodigoUnico(String codigoUnico) {
        // Normalizar el código: reemplazar barras (/) por guiones (-)
        String codigoNormalizado = codigoUnico.replace("/", "-");
        return prestamoRepository.findByHerramientaDetalle_CodigoUnicoAndFechaDevolucionIsNull(codigoNormalizado);
    }

    /**
     * Actualiza los datos de un préstamo existente
     * Verifica que el préstamo exista antes de actualizarlo
     */
    public Prestamo actualizarPrestamo(Integer idPrestamo, Prestamo prestamoActualizado) {
        if (!prestamoRepository.existsById(idPrestamo)) {
            throw new IllegalArgumentException("El préstamo con ID " + idPrestamo + " no existe.");
        }
        prestamoActualizado.setIdPrestamo(idPrestamo); // Asegúrate de que el ID se mantenga
        return prestamoRepository.save(prestamoActualizado);
    }

    /**
     * Elimina un préstamo del sistema por su ID
     * Verifica que el préstamo exista antes de eliminarlo
     */
    public void eliminarPrestamo(Integer idPrestamo) {
        if (!prestamoRepository.existsById(idPrestamo)) {
            throw new IllegalArgumentException("El préstamo con ID " + idPrestamo + " no existe.");
        }
        prestamoRepository.deleteById(idPrestamo);
    }

    /**
     * Obtiene todos los préstamos realizados por un usuario específico
     * Útil para ver el historial de préstamos de un usuario
     */
    public List<Prestamo> obtenerPrestamosPorUsuario(Integer idUsuario) {
        return prestamoRepository.findByUsuario_IdUsuario(idUsuario);
    }
}