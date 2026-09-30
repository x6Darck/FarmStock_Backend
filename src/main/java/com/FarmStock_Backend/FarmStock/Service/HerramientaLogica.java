package com.FarmStock_Backend.FarmStock.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;
import com.FarmStock_Backend.FarmStock.Model.Herramientas;
import com.FarmStock_Backend.FarmStock.Repository.Herramienta_detalleRepository;
import com.FarmStock_Backend.FarmStock.Repository.HerramientasRepository;

@Service
public class HerramientaLogica {

    private final HerramientasRepository herramientasRepository;
    private final Herramienta_detalleRepository herramientaDetalleRepository;

    public HerramientaLogica(HerramientasRepository herramientasRepository,
                             Herramienta_detalleRepository herramientaDetalleRepository) {
        this.herramientasRepository = herramientasRepository;
        this.herramientaDetalleRepository = herramientaDetalleRepository;
    }

    /**
     * Crea una herramienta y genera sus detalles (unidades físicas).
     * - Guarda la herramienta base.
     * - Crea N registros en detalle según 'cantidad' con código único:
     *   NOMBRE-MAYUS-ID_HERRAMIENTA-XXX y los marca como 'Disponible'.
     * - Genera QR y código de barras para cada detalle.
     */
    public Herramientas crearHerramienta(Herramientas herramienta) {
            Herramientas herramientaGuardada = herramientasRepository.save(herramienta);

            int cantidad = herramientaGuardada.getCantidad() != null ? herramientaGuardada.getCantidad() : 0;

            for (int i = 1; i <= cantidad; i++) {
                Herramienta_detalle detalle = new Herramienta_detalle();
                detalle.setHerramienta(herramientaGuardada);
                String codigo = herramientaGuardada.getNombre().toUpperCase() + "-" 
                + herramientaGuardada.getIdHerramienta() + "-" 
                + String.format("%03d", i);
                detalle.setCodigoUnico(codigo);
                detalle.setEstado("Disponible");
                detalle.setDisponible(true);
                detalle.setFechaIngreso(herramientaGuardada.getFechaRegistro());
                herramientaDetalleRepository.save(detalle);

                // Generar QR automáticamente
                HerramientaDetalleLogica.generarCodigoQR(codigo, "qr_" + codigo + ".png");
            }

            return herramientaGuardada;
        }

    /**
     * Lista herramientas registradas en la fecha actual (hoy).
     */
    public List<Herramientas> obtenerHerramientasDeHoy() {
        LocalDate hoy = LocalDate.now();
        return herramientasRepository.findByFechaRegistro(hoy);
    }

    /**
     * Lista todas las herramientas existentes.
     */
    public List<Herramientas> obtenerTodasHerramientas() {
        return herramientasRepository.findAll();
    }

    /**
     * Obtiene una herramienta por su ID o lanza error si no existe.
     */
    public Herramientas obtenerPorId(Integer id) {
        Optional<Herramientas> opt = herramientasRepository.findById(id);
        return opt.orElseThrow(() -> new IllegalArgumentException("No se encontró herramienta con id: " + id));
    }

    /**
     * Actualiza datos de la herramienta y sincroniza la cantidad con sus detalles:
     * - Solo permite AUMENTAR la cantidad, no disminuirla (para preservar historial).
     * - Si aumenta la cantidad, crea nuevos detalles con códigos QR/barras.
     * - Si se intenta disminuir, lanza excepción.
     */
    public Herramientas actualizarHerramienta(Integer id, Herramientas herramienta) {
        Optional<Herramientas> opt = herramientasRepository.findById(id);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("No se encontró la herramienta con id: " + id);
        }
        Herramientas existente = opt.get();

        existente.setNombre(herramienta.getNombre());
        existente.setDescripcion(herramienta.getDescripcion());
        existente.setEstado(herramienta.getEstado());
        existente.setTipo(herramienta.getTipo());
        existente.setUbicacion(herramienta.getUbicacion());
        existente.setNumeroLote(herramienta.getNumeroLote());
        existente.setFechaRegistro(herramienta.getFechaRegistro());
        int oldCantidad = existente.getCantidad() != null ? existente.getCantidad() : 0;
        int newCantidad = herramienta.getCantidad() != null ? herramienta.getCantidad() : 0;
        
        // Validar que solo se pueda aumentar la cantidad
        if (newCantidad < oldCantidad) {
            throw new IllegalArgumentException("No se puede disminuir la cantidad de herramientas. Cantidad actual: " + oldCantidad);
        }
        
        existente.setCantidad(newCantidad);

        Herramientas guardada = herramientasRepository.save(existente);

        // Solo crear nuevas unidades si aumentó la cantidad
        if (newCantidad > oldCantidad) {
            // Obtener los detalles existentes
            List<Herramienta_detalle> detallesExistentes = herramientaDetalleRepository
                .findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(guardada.getIdHerramienta());
            
            // Obtener los números de secuencia que ya existen
            List<Integer> numerosExistentes = detallesExistentes.stream()
                .map(d -> {
                    String codigo = d.getCodigoUnico();
                    String[] partes = codigo.split("-");
                    return Integer.parseInt(partes[partes.length - 1]);
                })
                .sorted()
                .toList();
            
            // Calcular cuántas unidades necesitamos crear
            int unidadesACrear = newCantidad - detallesExistentes.size();
            int numeroActual = 1;
            int creadas = 0;
            
            // Crear las nuevas unidades rellenando huecos primero
            while (creadas < unidadesACrear) {
                // Si este número no existe, crear la unidad
                if (!numerosExistentes.contains(numeroActual)) {
                    Herramienta_detalle detalle = new Herramienta_detalle();
                    detalle.setHerramienta(guardada);
                    String codigo = guardada.getNombre().toUpperCase() + "-" 
                       + guardada.getIdHerramienta() + "-" 
                       + String.format("%03d", numeroActual);
                    detalle.setCodigoUnico(codigo);
                    detalle.setEstado("Disponible");
                    detalle.setDisponible(true);
                    detalle.setFechaIngreso(guardada.getFechaRegistro());
                    herramientaDetalleRepository.save(detalle);
                    
                    // Generar QR
                    HerramientaDetalleLogica.generarCodigoQR(codigo, "qr_" + codigo + ".png");
                    
                    creadas++;
                }
                numeroActual++;
            }
        }

        return guardada;
    }    /**
     * Elimina una herramienta y sus detalles asociados.
     * - Primero borra los detalles para evitar referencias huérfanas.
     */
    public void eliminarHerramienta(Integer id) {
        Optional<Herramientas> opt = herramientasRepository.findById(id);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("No se encontró la herramienta con id: " + id);
        }
        List<Herramienta_detalle> detalles = herramientaDetalleRepository.findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(id);
        if (detalles != null && !detalles.isEmpty()) {
            herramientaDetalleRepository.deleteAll(detalles);
        }
        herramientasRepository.deleteById(id);
    }
}