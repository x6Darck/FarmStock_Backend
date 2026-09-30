package com.FarmStock_Backend.FarmStock.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.FarmStock_Backend.FarmStock.Model.ReporteDanio;

public interface ReporteDanioRepository extends JpaRepository<ReporteDanio, Integer> {
    // consultas personalizadas opcionales
}