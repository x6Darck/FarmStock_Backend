package com.FarmStock_Backend.FarmStock.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.FarmStock_Backend.FarmStock.Model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByIdUsuario(Integer idUsuario);
}