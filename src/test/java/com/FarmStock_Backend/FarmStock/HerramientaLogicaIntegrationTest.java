package com.FarmStock_Backend.FarmStock;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;
import com.FarmStock_Backend.FarmStock.Model.Herramientas;
import com.FarmStock_Backend.FarmStock.Repository.Herramienta_detalleRepository;
import com.FarmStock_Backend.FarmStock.Repository.HerramientasRepository;
import com.FarmStock_Backend.FarmStock.Service.HerramientaLogica;

@SpringBootTest
@Transactional // para que los datos de prueba se eliminen automáticamente al terminar
public class HerramientaLogicaIntegrationTest {

    @Autowired
    private HerramientaLogica herramientaLogica;

    @Autowired
    private HerramientasRepository herramientasRepository;

    @Autowired
    private Herramienta_detalleRepository herramientaDetalleRepository;

    @Test
    void testCrearHerramienta_creaDetallesCorrectamente() {
        // Arrange
        Herramientas h = new Herramientas();
        h.setNombre("Martillo");
        h.setDescripcion("Martillo de acero");
        h.setEstado("Activo");
        h.setTipo("Manual");
        h.setUbicacion("Bodega 1");
        h.setNumeroLote("L123");
        h.setFechaRegistro(LocalDate.now());
        h.setCantidad(3);

        // Act
        Herramientas guardada = herramientaLogica.crearHerramienta(h);

        // Assert
        assertNotNull(guardada.getIdHerramienta());
        List<Herramienta_detalle> detalles = herramientaDetalleRepository
                .findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(guardada.getIdHerramienta());

        assertEquals(3, detalles.size(), "Debe crear tres detalles para la herramienta");

        // Verifica el formato de los códigos únicos generados
        assertTrue(detalles.get(0).getCodigoUnico().startsWith("MARTILLO-"));
    }

    @Test
    void testActualizarHerramienta_agregaYEliminaDetalles() {
        // Arrange
        Herramientas h = new Herramientas();
        h.setNombre("Taladro");
        h.setDescripcion("Taladro industrial");
        h.setEstado("Activo");
        h.setTipo("Eléctrico");
        h.setUbicacion("Taller");
        h.setNumeroLote("L200");
        h.setFechaRegistro(LocalDate.now());
        h.setCantidad(2);

        Herramientas guardada = herramientaLogica.crearHerramienta(h);

        // Act 1: aumentar la cantidad a 4
        h.setCantidad(4);
        herramientaLogica.actualizarHerramienta(guardada.getIdHerramienta(), h);
        List<Herramienta_detalle> detallesAumentados = herramientaDetalleRepository
                .findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(guardada.getIdHerramienta());

        // Assert 1
        assertEquals(4, detallesAumentados.size(), "Debe aumentar a cuatro detalles");

        // Act 2: disminuir la cantidad a 2
        h.setCantidad(2);
        herramientaLogica.actualizarHerramienta(guardada.getIdHerramienta(), h);
        List<Herramienta_detalle> detallesReducidos = herramientaDetalleRepository
                .findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(guardada.getIdHerramienta());

        // Assert 2
        assertEquals(2, detallesReducidos.size(), "Debe eliminar dos detalles");
    }

    @Test
    void testEliminarHerramienta_eliminaDetallesYPrincipal() {
        // Arrange
        Herramientas h = new Herramientas();
        h.setNombre("Sierra");
        h.setDescripcion("Sierra eléctrica");
        h.setEstado("Activo");
        h.setTipo("Eléctrico");
        h.setUbicacion("Depósito");
        h.setNumeroLote("L300");
        h.setFechaRegistro(LocalDate.now());
        h.setCantidad(2);

        Herramientas guardada = herramientaLogica.crearHerramienta(h);

        // Act
        herramientaLogica.eliminarHerramienta(guardada.getIdHerramienta());

        // Assert
        assertFalse(herramientasRepository.findById(guardada.getIdHerramienta()).isPresent(),
                "La herramienta principal debe eliminarse");
        List<Herramienta_detalle> detalles = herramientaDetalleRepository
                .findByHerramienta_IdHerramientaOrderByCodigoUnicoAsc(guardada.getIdHerramienta());
        assertTrue(detalles.isEmpty(), "Los detalles también deben eliminarse");
    }
}
