package com.FarmStock_Backend.FarmStock.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "aprendiz")
public class Aprendiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aprendiz")
    private Integer idAprendiz;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El nombre solo puede contener letras")
    private String nombre;

    @Pattern(regexp = "^(CC|TI|PPT)?$", message = "Tipo de documento no válido. Opciones: CC, TI, PPT")
    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El número de documento debe contener solo dígitos")
    @Column(name = "numero_documento", unique = true)
    private String numeroDocumento;

    @NotBlank(message = "El número de ficha no puede estar vacío")
    @Pattern(regexp = "^[0-9A-Za-z-]+$", message = "El número de ficha debe contener solo letras, números o guiones")
    @Column(name = "numero_ficha")
    private String numeroFicha;

    // 🔹 Constructor vacío (obligatorio para JPA)
    public Aprendiz() {}

    // 🔹 Constructor con parámetros
    public Aprendiz(String nombre, String tipoDocumento, String numeroDocumento, String numeroFicha) {
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.numeroFicha = numeroFicha;
    }

    // 🔹 Getters y Setters
    public Integer getIdAprendiz() {
        return idAprendiz;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento.trim();
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }
    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = numeroFicha.trim();
    }

}
