package com.FarmStock_Backend.FarmStock.Controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.FarmStock_Backend.FarmStock.Service.ResendEmailService;

@RestController
@RequestMapping("/api/notificaciones")
public class EmailController {

    private final ResendEmailService resendService;

    public EmailController(ResendEmailService resendService) {
        this.resendService = resendService;
    }

    @PostMapping("/resend")
    public ResponseEntity<?> enviar(
            @RequestParam("destinatario") String destinatario,
            @RequestParam("asunto") String asunto,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "html", required = false) String html,
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) {
        try {
            String htmlBody = (html != null && !html.isBlank()) ? html : "<pre style=\"font-family:inherit\">" + escapeHtml(descripcion) + "</pre>";
            String textBody = descripcion != null ? descripcion : "";
            resendService.send(destinatario, asunto, htmlBody, textBody, files);
            return ResponseEntity.ok(Map.of("message", "Correo enviado con éxito"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (IOException ex) {
            // si Resend devolvió 422 u otro, lo propagamos con body para que frontend lo muestre
            String msg = ex.getMessage();
            if (msg != null && msg.contains("422")) {
                return ResponseEntity.status(422).body(Map.of("error", msg));
            }
            return ResponseEntity.status(502).body(Map.of("error", msg));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}