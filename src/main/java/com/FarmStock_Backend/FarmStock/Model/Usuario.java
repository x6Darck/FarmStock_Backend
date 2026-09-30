package com.FarmStock_Backend.FarmStock.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario") 
    private Integer idUsuario;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El nombre solo puede contener letras")
    private String nombres;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El apellido solo puede contener letras")
    private String apellidos;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo no es válido")
    private String correo;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
    private String telefono;

    @NotBlank(message = "El número de documento no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El número de documento debe contener solo dígitos")
    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¿?*.,:;_-]).{8,}$",
        message = "La contraseña debe tener mínimo 8 caracteres, incluir mayúscula, minúscula, número y carácter especial"
    )
    private String contrasena;

    @NotBlank(message = "El cargo no puede estar vacío")
    @Pattern(regexp = "^(instructor|aprendiz|administrador)$", message = "Cargo no válido. Opciones: instructor, aprendiz, administrador")
    private String cargo;

    @NotBlank(message = "El tipo de documento no puede estar vacío")
    @Pattern(regexp = "^(CC|TI|PPT)$", message = "Tipo de documento no válido. Opciones: CC, TI, PPT")
    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public Usuario() {}

    public Usuario(String nombres, String apellidos, String correo, String telefono,
                   String numeroDocumento, String cargo, String tipoDocumento, String contrasena) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.numeroDocumento = numeroDocumento;
        this.cargo = cargo;
        this.tipoDocumento = tipoDocumento;
        this.contrasena = contrasena;
    }

    // Getters y setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres.trim();
    }

    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos.trim();
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo.toLowerCase().trim();
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono.trim();
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento.trim();
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena.trim();
    }

    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo.toLowerCase().trim();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
