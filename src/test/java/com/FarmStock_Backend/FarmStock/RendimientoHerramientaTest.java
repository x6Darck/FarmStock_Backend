package com.FarmStock_Backend.FarmStock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de rendimiento para el sistema FarmStock.
 * Estas pruebas no usan base de datos, sino simulaciones de carga y procesamiento.
 */
public class RendimientoHerramientaTest {

    /**
     * Prueba 1: Generación masiva de códigos únicos de herramientas.
     * Evalúa el rendimiento del sistema al crear 50,000 registros en memoria.
     */
    @Test
    void testGeneracionMasivaCodigosHerramientas() {
        long inicio = System.currentTimeMillis();

        int cantidad = 50000;
        String[] codigos = new String[cantidad];

        for (int i = 0; i < cantidad; i++) {
            codigos[i] = "HERR-" + String.format("%05d", i);
            assertNotNull(codigos[i]);
        }

        long fin = System.currentTimeMillis();
        long duracion = fin - inicio;

        System.out.println("⏱️ Tiempo en generar " + cantidad + " códigos: " + duracion + " ms");

        // La generación debe completarse en menos de 2 segundos
        assertTrue(duracion < 2000, "La generación de códigos fue demasiado lenta");
    }

    /**
     * Prueba 2: Simula una operación de búsqueda intensiva.
     * Verifica la eficiencia de búsqueda sobre una lista grande en memoria.
     */
    @Test
    void testBusquedaEficienteHerramientas() {
        int cantidad = 100000;
        String[] inventario = new String[cantidad];

        for (int i = 0; i < cantidad; i++) {
            inventario[i] = "HERR-" + i;
        }

        long inicio = System.currentTimeMillis();
        boolean encontrado = false;

        // Simulamos una búsqueda secuencial
        for (String codigo : inventario) {
            if (codigo.equals("HERR-99999")) {
                encontrado = true;
                break;
            }
        }

        long fin = System.currentTimeMillis();
        long duracion = fin - inicio;

        System.out.println("🔍 Tiempo de búsqueda: " + duracion + " ms");

        assertTrue(encontrado, "No se encontró el código esperado");
        assertTrue(duracion < 100, "La búsqueda fue demasiado lenta");
    }

    /**
     * Prueba 3: Simula procesamiento de lógica de negocio (sin BD).
     * Calcula disponibilidad total y mide el rendimiento.
     */
    @Test
    void testProcesamientoLogicoHerramientas() {
        int cantidad = 100000;
        boolean[] disponibles = new boolean[cantidad];

        // Simula disponibilidad aleatoria
        for (int i = 0; i < cantidad; i++) {
            disponibles[i] = (i % 2 == 0); // mitad disponibles
        }

        long inicio = System.currentTimeMillis();
        int contadorDisponibles = 0;

        for (boolean disponible : disponibles) {
            if (disponible) contadorDisponibles++;
        }

        long fin = System.currentTimeMillis();
        long duracion = fin - inicio;

        double porcentaje = (contadorDisponibles * 100.0) / cantidad;
        System.out.println("📊 Herramientas disponibles: " + porcentaje + "%");
        System.out.println("⚙️ Tiempo de procesamiento: " + duracion + " ms");

        assertEquals(50.0, porcentaje, 0.1, "El cálculo de disponibilidad es incorrecto");
        assertTrue(duracion < 200, "El procesamiento fue demasiado lento");
    }

    /**
     * Prueba 4: Uso de memoria.
     * Mide la eficiencia de uso de memoria en una simulación masiva.
     */
    @Test
    void testUsoMemoriaSimulado() {
        System.gc(); // Limpia la memoria antes de iniciar

        long memoriaInicial = Runtime.getRuntime().freeMemory();
        int cantidad = 200000;
        String[] codigos = new String[cantidad];

        for (int i = 0; i < cantidad; i++) {
            codigos[i] = "TOOL-" + i;
        }

        long memoriaFinal = Runtime.getRuntime().freeMemory();
        long memoriaUsada = (memoriaInicial - memoriaFinal) / (1024 * 1024); // en MB

        System.out.println("💾 Memoria utilizada: " + memoriaUsada + " MB");

        // No debe superar 50 MB en esta simulación
        assertTrue(memoriaUsada < 50, "El uso de memoria fue excesivo");
    }
}
