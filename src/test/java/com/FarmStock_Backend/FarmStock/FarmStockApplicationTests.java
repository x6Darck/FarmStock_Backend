package com.FarmStock_Backend.FarmStock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FarmStock_Backend.FarmStock.Controller.Herramienta_DetalleController;
import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;
import com.FarmStock_Backend.FarmStock.Service.HerramientaDetalleLogica;

@ExtendWith(MockitoExtension.class)
class FarmStockApplicationTests {

    @Mock
    private HerramientaDetalleLogica herramientaDetalleLogica;

    @InjectMocks
    private Herramienta_DetalleController controller;

    @Test
    void testObtenerPorHerramienta() throws Exception {
        Herramienta_detalle detalle = new Herramienta_detalle();

        // Crear la entidad Herramientas y usar sus setters (los campos idHerramienta/cantidad pertenecen a Herramientas)
        com.FarmStock_Backend.FarmStock.Model.Herramientas herramienta = new com.FarmStock_Backend.FarmStock.Model.Herramientas();
        herramienta.setIdHerramienta(1);
        herramienta.setCantidad(5);
        detalle.setHerramienta(herramienta);

        when(herramientaDetalleLogica.obtenerHerramientas(1))
            .thenReturn(List.of(detalle));

        List<Herramienta_detalle> resultado = controller.obtenerPorHerramienta(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        // Leer cantidad desde la herramienta asociada
        Integer cantidad = resultado.get(0).getHerramienta().getCantidad();
        assertEquals(5, cantidad.intValue());
        verify(herramientaDetalleLogica).obtenerHerramientas(1);
    }
}
