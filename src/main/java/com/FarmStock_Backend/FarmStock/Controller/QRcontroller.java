package com.FarmStock_Backend.FarmStock.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Servidor de imágenes QR.
 *
 * - Intenta varias variantes (con/ sin prefijo "qr_", con extensiones png/jpg/jpeg).
 * - Busca en la carpeta configurada (propiedad qr.images.path, por defecto "codigos_qr")
 * - Evita path traversal y devuelve 404 si no existe.
 */
@RestController
@RequestMapping("/qr")
public class QRcontroller {

    private static final Logger logger = Logger.getLogger(QRcontroller.class.getName());

    @Value("${qr.images.path:codigos_qr}")
    private String qrDir; // carpeta por defecto relative al working dir; puedes configurarla en application.properties

    // extensiones a intentar si no vienen en el filename
    private static final List<String> DEFAULT_EXTS = Arrays.asList(".png", ".jpg", ".jpeg");

    /**
     * GET /qr/{filename:.+}
     * filename puede venir con o sin extensión y con o sin prefijo "qr_".
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<ByteArrayResource> getQrImage(@PathVariable String filename) {
        try {
            // Normalize input: evitar attempts de path traversal
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                logger.warning("Filename con contenido no válido: " + filename);
                return ResponseEntity.badRequest().build();
            }

            // Intentar exactamente el filename recibido primero
            Optional<Path> found = findFileVariant(filename);

            if (found.isEmpty()) {
                // intentar con prefijo 'qr_' si no lo tenía
                String withoutExt = stripExtension(filename);
                found = findFileVariant("qr_" + withoutExt);
            }

            if (found.isEmpty()) {
                logger.info("QR no encontrado para: " + filename + " (buscando en: " + qrDir + ")");
                return ResponseEntity.notFound().build();
            }

            Path file = found.get().normalize();
            byte[] data = Files.readAllBytes(file);

            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = MediaType.IMAGE_PNG_VALUE;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            // Mostrar inline para que el navegador lo renderice (no forzar descarga)
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFileName().toString() + "\"");
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(data));
        } catch (IOException e) {
            logger.severe("Error leyendo QR: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // busca variantes: con/ sin extensión, en qrDir
    private Optional<Path> findFileVariant(String base) {
        try {
            Path baseDir = Paths.get(qrDir).toAbsolutePath().normalize();

            // si base ya tiene extensión, probar directo
            String ext = getExtension(base);
            if (!ext.isEmpty()) {
                Path p = baseDir.resolve(base);
                if (Files.exists(p) && Files.isRegularFile(p)) return Optional.of(p);
            } else {
                // probar con cada extensión
                for (String e : DEFAULT_EXTS) {
                    Path p = baseDir.resolve(base + e);
                    if (Files.exists(p) && Files.isRegularFile(p)) return Optional.of(p);
                }
            }

            // además, intentar buscar exactamente el nombre (por si el archivo ya trae qr_ prefix)
            Path pExact = baseDir.resolve(base);
            if (Files.exists(pExact) && Files.isRegularFile(pExact)) return Optional.of(pExact);

            return Optional.empty();
        } catch (Exception ex) {
            logger.warning("findFileVariant fallo: " + ex.getMessage());
            return Optional.empty();
        }
    }

    private static String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) return filename.substring(i).toLowerCase();
        return "";
    }

    private static String stripExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0) return filename.substring(0,i);
        return filename;
    }
}