package com.FarmStock_Backend.FarmStock.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FarmStock_Backend.FarmStock.Model.ReporteDanio;
import com.FarmStock_Backend.FarmStock.Repository.ReporteDanioRepository;

@Service
public class ReporteDanioLogica {

    private final ReporteDanioRepository reporteDanioRepository;

    public ReporteDanioLogica(ReporteDanioRepository reporteDanioRepository) {
        this.reporteDanioRepository = reporteDanioRepository;
    }

    public ReporteDanio crearReporte(ReporteDanio reporte) {
        return reporteDanioRepository.save(reporte);
    }

    public List<ReporteDanio> verReportes() {
        return reporteDanioRepository.findAll();
    }

    public ReporteDanio buscarReporte(Integer id) {
        return reporteDanioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el reporte con id: " + id));
    }

    public ReporteDanio actualizarReporte(Integer id, ReporteDanio reporte) {
        Optional<ReporteDanio> opt = reporteDanioRepository.findById(id);
        if (opt.isPresent()) {
            ReporteDanio actual = opt.get();
            actual.setIdHerramienta(reporte.getIdHerramienta());
            actual.setDescripcion(reporte.getDescripcion());
            actual.setFechaReporte(reporte.getFechaReporte());
            actual.setReportadoPor(reporte.getReportadoPor());
            actual.setIdDetalle(reporte.getIdDetalle());
            return reporteDanioRepository.save(actual);
        } else {
            throw new IllegalArgumentException("No se encontró el reporte con id: " + id);
        }
    }

    public void eliminarReporte(Integer id) {
        if (reporteDanioRepository.existsById(id)) {
            reporteDanioRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No se encontró el reporte con id: " + id);
        }
    }
}