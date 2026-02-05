package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Evento;
import com.uniremington.semillero.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoServiceImpl eventoService;

    private Evento eventoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        eventoMock = new Evento();
        eventoMock.setId(1L);
        eventoMock.setTitulo("Conferencia de IA");
        eventoMock.setDescripcion("Evento sobre inteligencia artificial");
        eventoMock.setFecha(LocalDate.of(2025, 6, 15));
        eventoMock.setCategoria("tecnologia");
        eventoMock.setLugar("Auditorio Principal");
    }

    @Test
    @DisplayName("Debe listar todos los eventos")
    void testListarTodos() {
        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setTitulo("Feria de Empleo");

        when(eventoRepository.findAll()).thenReturn(Arrays.asList(eventoMock, evento2));

        List<Evento> resultado = eventoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(eventoRepository).findAll();
    }

    @Test
    @DisplayName("Debe listar eventos futuros")
    void testListarProximos() {
        LocalDate hoy = LocalDate.now();
        when(eventoRepository.findByFechaAfterOrderByFechaDesc(hoy))
                .thenReturn(Arrays.asList(eventoMock));

        List<Evento> resultado = eventoService.listarProximos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(eventoRepository).findByFechaAfterOrderByFechaDesc(any(LocalDate.class));
    }

    @Test
    @DisplayName("Debe filtrar eventos por categoría")
    void testListarPorCategoria() {
        String categoria = "tecnologia";
        when(eventoRepository.findByCategoria(categoria))
                .thenReturn(Arrays.asList(eventoMock));

        List<Evento> resultado = eventoService.listarPorCategoria(categoria);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("tecnologia", resultado.get(0).getCategoria());
        verify(eventoRepository).findByCategoria(categoria);
    }

    @Test
    @DisplayName("Debe buscar evento por ID")
    void testBuscarPorId() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(eventoMock));

        Optional<Evento> resultado = eventoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Conferencia de IA", resultado.get().getTitulo());
    }

    @Test
    @DisplayName("Debe guardar un evento")
    void testGuardar() {
        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoMock);

        Evento resultado = eventoService.guardar(eventoMock);

        assertNotNull(resultado);
        assertEquals("Conferencia de IA", resultado.getTitulo());
        verify(eventoRepository).save(eventoMock);
    }

    @Test
    @DisplayName("Debe eliminar un evento")
    void testEliminar() {
        doNothing().when(eventoRepository).deleteById(1L);

        eventoService.eliminar(1L);

        verify(eventoRepository).deleteById(1L);
    }
}