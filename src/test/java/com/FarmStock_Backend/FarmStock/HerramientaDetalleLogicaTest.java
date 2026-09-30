package com.FarmStock_Backend.FarmStock;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FarmStock_Backend.FarmStock.Model.Herramienta_detalle;
import com.FarmStock_Backend.FarmStock.Model.Herramientas;
import com.FarmStock_Backend.FarmStock.Repository.Herramienta_detalleRepository;
import com.FarmStock_Backend.FarmStock.Repository.HerramientasRepository;
import com.FarmStock_Backend.FarmStock.Service.HerramientaDetalleLogica;

@ExtendWith(MockitoExtension.class)
class HerramientaDetalleLogicaTest {

    @Mock
    private HerramientasRepository herramientasRepository;

    @Mock
    private Herramienta_detalleRepository herramienta_detalleRepository;


    @InjectMocks
    private HerramientaDetalleLogica herramientaDetalleLogica;

    @Test
    void testCrearHerramienta() {
        // 🔹 Crear una herramienta de prueba
        Herramientas herramienta = new Herramientas();
        herramienta.setIdHerramienta(1);
        herramienta.setNombre("Martillo");
        herramienta.setCantidad(3);
        herramienta.setFechaRegistro(LocalDate.now());

        // 🔹 Simular el guardado de la herramienta principal
        when(herramientasRepository.save(any(Herramientas.class)))
                .thenReturn(herramienta);

        // 🔹 Simular el guardado de los detalles
        when(herramienta_detalleRepository.save(any(Herramienta_detalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 🔹 Ejecutar el método a probar
        Herramientas resultado = herramientaDetalleLogica.crearHerramienta(herramienta);

        // 🔹 Verificar que la herramienta fue guardada una vez
        verify(herramientasRepository, times(1)).save(any(Herramientas.class));

        // 🔹 Verificar que los detalles se guardaron según la cantidad
        ArgumentCaptor<Herramienta_detalle> detalleCaptor =
                ArgumentCaptor.forClass(Herramienta_detalle.class);
        verify(herramienta_detalleRepository, times(3)).save(detalleCaptor.capture());

        List<Herramienta_detalle> detallesGuardados = detalleCaptor.getAllValues();

        // 🔹 Verificar que los códigos generados sean correctos
        assertEquals(3, detallesGuardados.size());
        assertEquals("MARTILLO-1-001", detallesGuardados.get(0).getCodigoUnico());
        assertEquals("MARTILLO-1-002", detallesGuardados.get(1).getCodigoUnico());
        assertEquals("MARTILLO-1-003", detallesGuardados.get(2).getCodigoUnico());

        // 🔹 Verificar otros campos de los detalles
        for (Herramienta_detalle detalle : detallesGuardados) {
            assertEquals("Disponible", detalle.getEstado());
            assertTrue(detalle.getDisponible());
            assertEquals(herramienta.getFechaRegistro(), detalle.getFechaIngreso());
            assertEquals(herramienta, detalle.getHerramienta());
        }

        // 🔹 Verificar que la herramienta devuelta no sea nula
        assertNotNull(resultado);
        assertEquals("Martillo", resultado.getNombre());
        assertEquals(3, resultado.getCantidad());
    }
}
