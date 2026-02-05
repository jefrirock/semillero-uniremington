package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.repository.DocenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;

    @InjectMocks
    private DocenteServiceImpl docenteService;

    private Docente docenteMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        docenteMock = new Docente();
        docenteMock.setId(1L);
        docenteMock.setNombre("Carlos Andrés Pérez");
        docenteMock.setArea("Tecnología");
        docenteMock.setEmail("carlos.perez@uniremington.edu.co");
    }

    @Test
    @DisplayName("Debe listar todos los docentes")
    void testListarTodos() {
        Docente docente2 = new Docente();
        docente2.setId(2L);
        docente2.setNombre("María Fernanda López");

        when(docenteRepository.findAll()).thenReturn(Arrays.asList(docenteMock, docente2));

        List<Docente> resultado = docenteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Carlos Andrés Pérez", resultado.get(0).getNombre());
        verify(docenteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar docente por ID cuando existe")
    void testBuscarPorIdExistente() {
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docenteMock));

        Optional<Docente> resultado = docenteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Carlos Andrés Pérez", resultado.get().getNombre());
        verify(docenteRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando docente no existe")
    void testBuscarPorIdNoExistente() {
        when(docenteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Docente> resultado = docenteService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
        verify(docenteRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar un docente")
    void testGuardar() {
        Docente nuevoDocente = new Docente();
        nuevoDocente.setNombre("Ana María García");
        nuevoDocente.setArea("Ingeniería");

        when(docenteRepository.save(any(Docente.class))).thenReturn(nuevoDocente);

        Docente resultado = docenteService.guardar(nuevoDocente);

        assertNotNull(resultado);
        assertEquals("Ana María García", resultado.getNombre());
        verify(docenteRepository).save(nuevoDocente);
    }

    @Test
    @DisplayName("Debe eliminar un docente")
    void testEliminar() {
        doNothing().when(docenteRepository).deleteById(1L);

        docenteService.eliminar(1L);

        verify(docenteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe buscar docentes por nombre o área")
    void testBuscar() {
        // Given
        String busqueda = "Tecnología";

        // El servicio llama AMBOS métodos del repository
        when(docenteRepository.findByNombreContainingIgnoreCase(busqueda))
                .thenReturn(Arrays.asList());
        when(docenteRepository.findByAreaContainingIgnoreCase(busqueda))
                .thenReturn(Arrays.asList(docenteMock));

        // When
        List<Docente> resultado = docenteService.buscar(busqueda);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tecnología", resultado.get(0).getArea());

        // Verifica que se llamaron AMBOS métodos
        verify(docenteRepository).findByNombreContainingIgnoreCase(busqueda);
        verify(docenteRepository).findByAreaContainingIgnoreCase(busqueda);
    }
}