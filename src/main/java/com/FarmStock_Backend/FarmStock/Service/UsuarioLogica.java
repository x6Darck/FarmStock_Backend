package com.FarmStock_Backend.FarmStock.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.Model.Usuario;
import com.FarmStock_Backend.FarmStock.Repository.UsuarioRepository;

@Service
public class UsuarioLogica {
    
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioLogica(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Crea (registra) un nuevo usuario en el sistema.
     */
    public Usuario crearUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario por ID.
     * Lanza error si el usuario no existe.
     */
    public Usuario eliminarUsuario(Integer id ){
        if(usuarioRepository.existsById(id)){
            usuarioRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No se encontró el usuario con id: " + id);
        }
        return null;
    }
    
    /**
     * Retorna la lista de todos los usuarios.
     */
    public List<Usuario> verUsuarios(){
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su ID o lanza error si no existe.
     */
    public Usuario buscarUsuario(Integer id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id: " + id));
    }

    /**
     * Busca un usuario por su número de documento.
     */
    public Usuario buscarPorNumeroDocumento(String numeroDocumento){
        return usuarioRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con documento: " + numeroDocumento));
    }

    /**
     * Actualiza los datos del usuario indicado por ID.
     * Valida que el correo no esté siendo usado por otro usuario.
     */
    public Usuario actualizarUsuario(Integer id, Usuario usuario){
        Optional<Usuario> usuOptional = usuarioRepository.findById(id);

        if(usuOptional.isPresent()){
            Usuario usuario1 = usuOptional.get();

            // Validar que el correo no esté siendo usado por otro usuario
            if (usuario.getCorreo() != null && !usuario.getCorreo().equals(usuario1.getCorreo())) {
                Optional<Usuario> usuarioConCorreo = usuarioRepository.findByCorreo(usuario.getCorreo());
                if (usuarioConCorreo.isPresent() && !usuarioConCorreo.get().getIdUsuario().equals(id)) {
                    throw new IllegalArgumentException("El correo ya está siendo usado por otro usuario");
                }
            }

            usuario1.setNombres(usuario.getNombres());
            usuario1.setApellidos(usuario.getApellidos());
            usuario1.setCorreo(usuario.getCorreo());
            usuario1.setTelefono(usuario.getTelefono());
            usuario1.setNumeroDocumento(usuario.getNumeroDocumento());
            usuario1.setContrasena(usuario.getContrasena());
            usuario1.setCargo(usuario.getCargo());
            usuario1.setTipoDocumento(usuario.getTipoDocumento());

            return usuarioRepository.save(usuario1);
        } else {
            throw new IllegalArgumentException("No se encontró el usuario con id: " + id);
        }
    }


    /**
     * Valida las credenciales del usuario y sus atributos básicos de acceso.
     * Verifica: contraseña, tipo de documento y cargo.
     */
    public String login(String numeroDocumento, String contrasena, String tipoDocumento, String cargo){
        Usuario usuario1 = usuarioRepository.findByNumeroDocumento(numeroDocumento)
            .orElseThrow(() -> new IllegalArgumentException("Numero no encontrado"));
        
        if (!usuario1.getContrasena().equals(contrasena)) {
            throw new IllegalArgumentException("Contraseña Incorrecta");
        }

        if (!usuario1.getTipoDocumento().equals(tipoDocumento)){
            throw new IllegalArgumentException("Tipo de documento no valido");
        }

        if (!usuario1.getCargo().equals(cargo)){
            throw new IllegalArgumentException("Tipo de cargo no valido");
        }

        return "Iniciando sesión";
    }

    


}


