package com.FarmStock_Backend.FarmStock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FarmStock_Backend.FarmStock.Model.Aprendiz;
import com.FarmStock_Backend.FarmStock.Repository.AprendizRepository;
import com.FarmStock_Backend.FarmStock.Repository.PrestamoRepository;
import com.FarmStock_Backend.FarmStock.Service.AprendizLogica;

@ExtendWith(MockitoExtension.class)
class AprendizLogicaTest {

    @Mock
    private AprendizRepository aprendizRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private AprendizLogica aprendizLogica;

    private Aprendiz aprendizTest;

    @BeforeEach
    void setUp() {
        aprendizTest = new Aprendiz();
        aprendizTest.setNombre("Juan Pérez");
        aprendizTest.setTipoDocumento("CC");
        aprendizTest.setNumeroDocumento("123456789");
        aprendizTest.setNumeroFicha("2558933");
    }

    @Test
    void testCrearAprendiz_exitoso() {
        // Arrange
        when(aprendizRepository.findByNumeroDocumento(aprendizTest.getNumeroDocumento()))
            .thenReturn(Optional.empty());
        when(aprendizRepository.save(any(Aprendiz.class))).thenReturn(aprendizTest);

        // Act
        Aprendiz resultado = aprendizLogica.crearAprendiz(aprendizTest);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(aprendizRepository, times(1)).findByNumeroDocumento("123456789");
        verify(aprendizRepository, times(1)).save(aprendizTest);
    }

    @Test
    void testCrearAprendiz_documentoDuplicado() {
        // Arrange
        when(aprendizRepository.findByNumeroDocumento(aprendizTest.getNumeroDocumento()))
            .thenReturn(Optional.of(aprendizTest));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aprendizLogica.crearAprendiz(aprendizTest)
        );

        assertTrue(exception.getMessage().contains("Ya existe un aprendiz"));
        verify(aprendizRepository, times(1)).findByNumeroDocumento("123456789");
        verify(aprendizRepository, never()).save(any());
    }

    @Test
    void testVerAprendices() {
        // Arrange
        Aprendiz aprendiz2 = new Aprendiz();
        aprendiz2.setNombre("María López");
        List<Aprendiz> listaAprendices = Arrays.asList(aprendizTest, aprendiz2);
        
        when(aprendizRepository.findAll()).thenReturn(listaAprendices);

        // Act
        List<Aprendiz> resultado = aprendizLogica.verAprendices();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        assertEquals("María López", resultado.get(1).getNombre());
        verify(aprendizRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorDocumento_encontrado() {
        // Arrange
        when(aprendizRepository.findByTipoDocumentoAndNumeroDocumento("CC", "123456789"))
            .thenReturn(Optional.of(aprendizTest));

        // Act
        Aprendiz resultado = aprendizLogica.buscarPorDocumento("CC", "123456789");

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("123456789", resultado.getNumeroDocumento());
        verify(aprendizRepository, times(1)).findByTipoDocumentoAndNumeroDocumento("CC", "123456789");
    }

    @Test
    void testBuscarPorDocumento_noEncontrado() {
        // Arrange
        when(aprendizRepository.findByTipoDocumentoAndNumeroDocumento("CC", "999999999"))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aprendizLogica.buscarPorDocumento("CC", "999999999")
        );

        assertTrue(exception.getMessage().contains("No se encontró"));
        verify(aprendizRepository, times(1)).findByTipoDocumentoAndNumeroDocumento("CC", "999999999");
    }

    @Test
    void testBuscarPorNumeroFicha_encontrado() {
        // Arrange
        when(aprendizRepository.findByNumeroFicha("2558933"))
            .thenReturn(Optional.of(aprendizTest));

        // Act
        Aprendiz resultado = aprendizLogica.buscarPorNumeroFicha("2558933");

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("2558933", resultado.getNumeroFicha());
        verify(aprendizRepository, times(1)).findByNumeroFicha("2558933");
    }

    @Test
    void testBuscarPorNumeroFicha_noEncontrado() {
        // Arrange
        when(aprendizRepository.findByNumeroFicha("9999999"))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aprendizLogica.buscarPorNumeroFicha("9999999")
        );

        assertTrue(exception.getMessage().contains("No se encontró a ningun aprendiz"));
        verify(aprendizRepository, times(1)).findByNumeroFicha("9999999");
    }

    @Test
    void testActualizarAprendiz_exitoso() {
        // Arrange
        Aprendiz aprendizActualizado = new Aprendiz();
        aprendizActualizado.setNombre("Juan Carlos Pérez");
        aprendizActualizado.setTipoDocumento("CC");
        aprendizActualizado.setNumeroDocumento("123456789");
        aprendizActualizado.setNumeroFicha("2558933");

        when(aprendizRepository.findById(1)).thenReturn(Optional.of(aprendizTest));
        when(aprendizRepository.save(any(Aprendiz.class))).thenReturn(aprendizActualizado);

        // Act
        Aprendiz resultado = aprendizLogica.actualizarAprendiz(1, aprendizActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Carlos Pérez", resultado.getNombre());
        verify(aprendizRepository, times(1)).findById(1);
        verify(aprendizRepository, times(1)).save(any(Aprendiz.class));
    }

    @Test
    void testActualizarAprendiz_noEncontrado() {
        // Arrange
        when(aprendizRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aprendizLogica.actualizarAprendiz(999, aprendizTest)
        );

        assertTrue(exception.getMessage().contains("No se encontró el aprendiz"));
        verify(aprendizRepository, times(1)).findById(999);
        verify(aprendizRepository, never()).save(any());
    }

    @Test
    void testEliminarAprendiz_exitoso() {
        // Arrange
        when(aprendizRepository.existsById(1)).thenReturn(true);
        doNothing().when(prestamoRepository).deleteByAprendizIdAprendiz(1);
        doNothing().when(aprendizRepository).deleteById(1);

        // Act
        aprendizLogica.eliminarAprendiz(1);

        // Assert
        verify(aprendizRepository, times(1)).existsById(1);
        verify(prestamoRepository, times(1)).deleteByAprendizIdAprendiz(1);
        verify(aprendizRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminarAprendiz_noEncontrado() {
        // Arrange
        when(aprendizRepository.existsById(999)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aprendizLogica.eliminarAprendiz(999)
        );

        assertTrue(exception.getMessage().contains("No se encontró el aprendiz"));
        verify(aprendizRepository, times(1)).existsById(999);
        verify(prestamoRepository, never()).deleteByAprendizIdAprendiz(anyInt());
        verify(aprendizRepository, never()).deleteById(anyInt());
    }
}
