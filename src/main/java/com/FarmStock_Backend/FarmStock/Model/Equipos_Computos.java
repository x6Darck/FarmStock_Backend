package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "equipos_computos")
public class Equipos_Computos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    @JsonProperty("id_equipo")
    private Integer idEquipo;

    @NotBlank(message = "El nombre de la persona es requerido")
    @Column(name = "nombre_persona", nullable = false, length = 255)
    @JsonProperty("nombre_persona")
    private String nombrePersona;

    @NotBlank(message = "La cédula es requerida")
    @Column(name = "cedula", nullable = false, length = 50)
    private String cedula;

    @NotBlank(message = "El nombre del equipo es requerido")
    @Column(name = "nombre_equipo", nullable = false, length = 255)
    @JsonProperty("nombre_equipo")
    private String nombreEquipo;

    @NotBlank(message = "El código del equipo es requerido")
    @Column(name = "codigo_equipo", nullable = false, unique = true, length = 50)
    @JsonProperty("codigo_equipo")
    private String codigoEquipo;

    @NotNull(message = "La fecha de registro es requerida")
    @Column(name = "fecha_registro", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fecha_registro")
    private LocalDateTime fechaRegistro;

    public Equipos_Computos() {
    }

    public Equipos_Computos(String nombrePersona, String cedula, String nombreEquipo, 
                            String codigoEquipo, LocalDateTime fechaRegistro) {
        this.nombrePersona = nombrePersona;
        this.cedula = cedula;
        this.nombreEquipo = nombreEquipo;
        this.codigoEquipo = codigoEquipo;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Equipos_Computos{" +
                "idEquipo=" + idEquipo +
                ", nombrePersona='" + nombrePersona + '\'' +
                ", cedula='" + cedula + '\'' +
                ", nombreEquipo='" + nombreEquipo + '\'' +
                ", codigoEquipo='" + codigoEquipo + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
