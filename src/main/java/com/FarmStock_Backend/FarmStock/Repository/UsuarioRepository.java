package com.FarmStock_Backend.FarmStock.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FarmStock_Backend.FarmStock.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombres(String nombres);
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);
    Optional<Usuario> findByContrasena(String contrasena);
    Optional<Usuario> findByCorreo(String correo);
}
