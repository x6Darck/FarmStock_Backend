package com.FarmStock_Backend.FarmStock.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.Model.Notificacion;
import com.FarmStock_Backend.FarmStock.Repository.NotificacionRepository;

@Service
public class NotificacionLogica {

    private final NotificacionRepository notificacionRepository;

    public NotificacionLogica(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public Notificacion crearNotificacion(Notificacion notificacion) {
        if (notificacion.getFecha() == null) {
            notificacion.setFecha(LocalDateTime.now());
        }
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> verNotificaciones() {
        return notificacionRepository.findAll();
    }

    public List<Notificacion> verNotificacionesPorUsuario(Integer idUsuario) {
        return notificacionRepository.findByIdUsuario(idUsuario);
    }

    public Notificacion buscarNotificacion(Integer id) {
        return notificacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la notificación con id: " + id));
    }

    public Notificacion actualizarNotificacion(Integer id, Notificacion notificacion) {
        Optional<Notificacion> opt = notificacionRepository.findById(id);
        if (opt.isPresent()) {
            Notificacion actual = opt.get();
            actual.setMensaje(notificacion.getMensaje());
            actual.setTipo(notificacion.getTipo());
            actual.setLeida(notificacion.getLeida());
            // no sobreescribo fecha a menos que venga
            if (notificacion.getFecha() != null) {
                actual.setFecha(notificacion.getFecha());
            }
            return notificacionRepository.save(actual);
        } else {
            throw new IllegalArgumentException("No se encontró la notificación con id: " + id);
        }
    }

    public void eliminarNotificacion(Integer id) {
        if (notificacionRepository.existsById(id)) {
            notificacionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No se encontró la notificación con id: " + id);
        }
    }
}