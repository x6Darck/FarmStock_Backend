package com.FarmStock_Backend.FarmStock.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servicio Resend (HTTP) - versión que FORZAMENTE envía "from" como STRING.
 * Asegúrate de que resend.from esté en application.properties o RESEND_FROM en env:
 * - "Acme <onboarding@resend.dev>"  (recomendado)
 * - "onboarding@resend.dev"
 */
@Service
public class ResendEmailService {

    @Value("${resend.api.key:}")
    private String apiKey;

    @Value("${resend.from:}")
    private String resendFromRaw;

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final long MAX_BYTES_PER_FILE = 10L * 1024 * 1024; // 10 MB
    private static final int MAX_FILES = 5;

    private static final Pattern EMAIL_IN_ANGLE = Pattern.compile("^\\s*(.+)\\s*<([^>]+)>\\s*$");
    private static final Pattern EMAIL_ONLY = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME_SPACE_EMAIL = Pattern.compile("^\\s*(.+)\\s+([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})\\s*$");

    public void send(String to, String subject, String htmlBody, String textBody, MultipartFile[] files) throws IOException, InterruptedException {
        // fallback env vars
        if ((apiKey == null || apiKey.isBlank()) && System.getenv("RESEND_API_KEY") != null) {
            apiKey = System.getenv("RESEND_API_KEY");
        }
        if ((resendFromRaw == null || resendFromRaw.isBlank()) && System.getenv("RESEND_FROM") != null) {
            resendFromRaw = System.getenv("RESEND_FROM");
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("RESEND_API_KEY no configurada.");
        }

        // normalizar y obtener string for "from"
        String fromString = buildFromString();

        if (to == null || to.isBlank() || !EMAIL_ONLY.matcher(to).matches()) {
            throw new IllegalArgumentException("Destinatario inválido: " + to);
        }

        Map<String,Object> payload = new HashMap<>();
        // IMPORTANT: send "from" as STRING (Resend expects a string here)
        payload.put("from", fromString);
        payload.put("to", List.of(to));
        payload.put("subject", subject != null ? subject : "");
        if (htmlBody != null && !htmlBody.isEmpty()) payload.put("html", htmlBody);
        if (textBody != null && !textBody.isEmpty()) payload.put("text", textBody);

        if (files != null && files.length > 0) {
            List<Map<String,String>> atts = new ArrayList<>();
            int count = 0;
            for (MultipartFile f : files) {
                if (f == null || f.isEmpty()) continue;
                if (++count > MAX_FILES) break;
                if (f.getSize() > MAX_BYTES_PER_FILE) {
                    throw new IllegalArgumentException("Archivo demasiado grande: " + f.getOriginalFilename());
                }
                String base64 = Base64.getEncoder().encodeToString(f.getBytes());
                Map<String,String> att = new HashMap<>();
                att.put("type", Optional.ofNullable(f.getContentType()).orElse("application/octet-stream"));
                att.put("name", Optional.ofNullable(f.getOriginalFilename()).orElse("adjunto"));
                att.put("data", base64);
                atts.add(att);
            }
            if (!atts.isEmpty()) payload.put("attachments", atts);
        }

        String body = mapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.resend.com/emails"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) {
            throw new IOException("Error Resend: " + resp.statusCode() + " - " + resp.body());
        }
    }

    // Genera un string "from" válido: "Name <email>" o "email"
    private String buildFromString() {
        if (resendFromRaw == null || resendFromRaw.isBlank()) {
            throw new IllegalArgumentException("No está configurado resend.from. Define resend.from en application.properties o RESEND_FROM en env.");
        }
        String cleaned = normalizeRawFrom(resendFromRaw);

        Matcher mAngle = EMAIL_IN_ANGLE.matcher(cleaned);
        if (mAngle.matches()) {
            String name = mAngle.group(1).trim();
            String email = mAngle.group(2).trim();
            if (!EMAIL_ONLY.matcher(email).matches())
                throw new IllegalArgumentException("Email en resend.from inválido: " + email);
            // devolver string con angle brackets
            return name + " <" + email + ">";
        }

        Matcher mSpace = NAME_SPACE_EMAIL.matcher(cleaned);
        if (mSpace.matches()) {
            String name = mSpace.group(1).trim();
            String email = mSpace.group(2).trim();
            if (!EMAIL_ONLY.matcher(email).matches())
                throw new IllegalArgumentException("Email en resend.from inválido: " + email);
            return name + " <" + email + ">";
        }

        // si es solo email
        if (EMAIL_ONLY.matcher(cleaned).matches()) {
            return cleaned;
        }

        throw new IllegalArgumentException("resend.from inválido. Debe ser 'email@example.com' o 'Name <email@example.com>'");
    }

    private String normalizeRawFrom(String raw) {
        String s = raw == null ? "" : raw.trim();
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            s = s.substring(1, s.length() - 1).trim();
        }
        return s;
    }
}