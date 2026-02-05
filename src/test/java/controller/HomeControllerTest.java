package com.uniremington.semillero.controller;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.model.Evento;
import com.uniremington.semillero.model.Noticia;
import com.uniremington.semillero.repository.FotoEventoRepository;
import com.uniremington.semillero.repository.NoticiaRepository;
import com.uniremington.semillero.service.DocenteService;
import com.uniremington.semillero.service.EventoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocenteService docenteService;

    @MockBean
    private EventoService eventoService;

    @MockBean
    private NoticiaRepository noticiaRepository;

    @MockBean
    private FotoEventoRepository fotoEventoRepository;

    @Test
    @DisplayName("Debe cargar la página de inicio")
    void testIndexPage() throws Exception {
        List<Docente> docentes = Arrays.asList(new Docente(), new Docente());
        List<Evento> eventos = Arrays.asList(new Evento());
        List<Noticia> noticias = Arrays.asList(new Noticia());

        when(docenteService.listarTodos()).thenReturn(docentes);
        when(eventoService.listarProximos()).thenReturn(eventos);
        when(noticiaRepository.findByActivaTrueOrderByOrdenAsc()).thenReturn(noticias);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("Debe cargar la página de docentes")
    void testDocentesPage() throws Exception {
        when(docenteService.listarTodos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/docentes"))
                .andExpect(status().isOk())
                .andExpect(view().name("docentes"));
    }

    @Test
    @DisplayName("Debe cargar la página de eventos")
    void testEventosPage() throws Exception {
        when(eventoService.listarProximos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/eventos"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventos"));
    }

    @Test
    @DisplayName("Debe filtrar eventos por categoría")
    void testEventosFiltrados() throws Exception {
        String categoria = "tecnologia";
        when(eventoService.listarPorCategoria(categoria)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/eventos").param("categoria", categoria))
                .andExpect(status().isOk())
                .andExpect(view().name("eventos"))
                .andExpect(model().attribute("categoriaActual", categoria));
    }

    @Test
    @DisplayName("Debe cargar formulario de nuevo evento")
    void testFormularioEvento() throws Exception {
        mockMvc.perform(get("/eventos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("formulario-evento"));
    }

    @Test
    @DisplayName("Debe eliminar evento y redirigir")
    void testEliminarEvento() throws Exception {
        doNothing().when(eventoService).eliminar(1L);
        when(fotoEventoRepository.findByEventoId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/eventos/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/eventos"));
    }
}