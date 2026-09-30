package com.FarmStock_Backend.FarmStock.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;
import com.FarmStock_Backend.FarmStock.Model.Herramientas;
import com.FarmStock_Backend.FarmStock.Repository.Herramienta_detalleRepository;
import com.FarmStock_Backend.FarmStock.Repository.HerramientasRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class HerramientaDetalleLogica {

    private final Herramienta_detalleRepository herramientaDetalleRepository;
    private final HerramientasRepository herramientasRepository;

    public HerramientaDetalleLogica(Herramienta_detalleRepository herramientaDetalleRepository,
                                    HerramientasRepository herramientasRepository) {
        this.herramientaDetalleRepository = herramientaDetalleRepository;
        this.herramientasRepository = herramientasRepository;
    }

    //  Actualizar herramienta detalle
    public Herramienta_detalle actualizarHerramientaDetalle(Integer id, Herramienta_detalle detalleActualizado) {
        Optional<Herramienta_detalle> detalleOptional = herramientaDetalleRepository.findById(id);

        if (detalleOptional.isPresent()) {
            Herramienta_detalle detalleExistente = detalleOptional.get();

            detalleExistente.setEstado(detalleActualizado.getEstado());
            detalleExistente.setDisponible(detalleActualizado.getDisponible());
            detalleExistente.getFechaIngreso();
            detalleExistente.setComentario(detalleActualizado.getComentario());
            

            return herramientaDetalleRepository.save(detalleExistente);
        } else {
            throw new IllegalArgumentException("No se encontró el detalle de herramienta con id: " + id);
        }
    }

    //  Obtener todos los detalles de herramientas
    public List<Herramienta_detalle> obtenerTodos() {
        return herramientaDetalleRepository.findAll();
    }

    //  Obtener herramientas por ID
    public List<Herramienta_detalle> obtenerHerramientas(Integer id) {
        return herramientaDetalleRepository.findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(id);
    }

    //  Obtener detalle por código único
    public Herramienta_detalle obtenerPorCodigoUnico(String codigoUnico) {
        return herramientaDetalleRepository.findByCodigoUnico(codigoUnico)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró herramienta con código: " + codigoUnico));
    }

    //  Eliminar una unidad física específica (herramienta_detalle)
    public void eliminarHerramientaDetalle(Integer idDetalle) {
        Optional<Herramienta_detalle> detalleOpt = herramientaDetalleRepository.findById(idDetalle);
        
        if (!detalleOpt.isPresent()) {
            throw new IllegalArgumentException("No se encontró la herramienta detalle con id: " + idDetalle);
        }
        
        Herramienta_detalle detalle = detalleOpt.get();
        Herramientas herramienta = detalle.getHerramienta();
        
        try {
            // Intentar eliminar el detalle
            herramientaDetalleRepository.deleteById(idDetalle);
            
            // Si se eliminó exitosamente, actualizar la cantidad en la herramienta general
            int cantidadActual = herramienta.getCantidad() != null ? herramienta.getCantidad() : 0;
            if (cantidadActual > 0) {
                herramienta.setCantidad(cantidadActual - 1);
                herramientasRepository.save(herramienta);
            }
        } catch (Exception e) {
            // Si falla por restricción de clave foránea (tiene préstamos/mantenimientos)
            throw new IllegalArgumentException(
                "No se puede eliminar esta herramienta porque tiene historial de préstamos o mantenimientos asociados. " +
                "Solo se pueden eliminar herramientas sin historial."
            );
        }
    }

    // 🛠️ Crear herramienta y generar códigos
    public Herramientas crearHerramienta(Herramientas herramienta) {
        Herramientas saved = herramientasRepository.save(herramienta);

        Integer cantidad = saved.getCantidad() != null ? saved.getCantidad() : 0;
        String nombreUpper = saved.getNombre() != null ? saved.getNombre().toUpperCase() : "HERRAMIENTA";
        Integer idHerr = saved.getIdHerramienta() != null ? saved.getIdHerramienta() : 0;

        for (int i = 1; i <= cantidad; i++) {
            Herramienta_detalle detalle = new Herramienta_detalle();
            detalle.setHerramienta(saved);
            detalle.setEstado("Disponible");
            detalle.setDisponible(true);
            detalle.setFechaIngreso(saved.getFechaRegistro());

            // Generar código único
            String codigo = String.format("%s-%d-%03d", nombreUpper, idHerr, i);
            detalle.setCodigoUnico(codigo);

            // Guardar el detalle
            herramientaDetalleRepository.save(detalle);

            // Generar QR automáticamente
            generarCodigoQR(codigo, "qr_" + codigo + ".png");
        }

        return saved;
    }

    // 🧾 Generar código QR
    public static void generarCodigoQR(String texto, String nombreArchivo) {
        try {
            int width = 250;
            int height = 250;

            BitMatrix matrix = new MultiFormatWriter().encode(texto, BarcodeFormat.QR_CODE, width, height);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

            File directorio = new File("codigos_qr");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            File outputFile = new File(directorio, nombreArchivo);
            ImageIO.write(image, "png", outputFile);

            System.out.println("✅ Código QR generado: " + outputFile.getAbsolutePath());
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el archivo de imagen QR para un código único específico.
     * Retorna el archivo para que pueda ser enviado como respuesta HTTP.
     */
    public File obtenerImagenQR(String codigoUnico) {
        String nombreArchivo = "qr_" + codigoUnico + ".png";
        File archivoQR = new File("codigos_qr", nombreArchivo);
        
        if (!archivoQR.exists()) {
            throw new IllegalArgumentException("No se encontró el código QR para: " + codigoUnico);
        }
        
        return archivoQR;
    }
}
