package com.uniremington.semillero;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.repository.DocenteRepository;
import com.uniremington.semillero.service.DocenteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;

    @InjectMocks
    private DocenteServiceImpl docenteService;

    private Docente docente1;
    private Docente docente2;

    @BeforeEach
    void setUp() {
        docente1 = new Docente(1L, "Miguel Madarriaga", "Ingeniería",
                "Redes", "miguel@uni.edu", "300-506-43-58", 10, "img1.jpg");
        docente2 = new Docente(2L, "Edier Avilés", "Ingeniería",
                "Bases de Datos", "edier@uni.edu", "322-632-74-44", 5, "img2.jpg");
    }

    @Test
    void listarTodos_DebeRetornarListaCompleta() {
        // Given (Dado)
        when(docenteRepository.findAll()).thenReturn(Arrays.asList(docente1, docente2));

        // When (Cuando)
        List<Docente> resultado = docenteService.listarTodos();

        // Then (Entonces)
        assertEquals(2, resultado.size());
        verify(docenteRepository, times(1)).findAll();
    }

    @Test
    void buscarPorArea_DebeFiltrarCorrectamente() {
        // Given
        when(docenteRepository.findByAreaContainingIgnoreCase("Ingeniería"))
                .thenReturn(Arrays.asList(docente1, docente2));

        // When
        List<Docente> resultado = docenteService.buscarPorArea("Ingeniería");

        // Then
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(d -> d.getArea().equals("Ingeniería")));
    }

    @Test
    void guardar_DebePersistirDocente() {
        // Given
        Docente nuevo = new Docente(null, "Nuevo Profesor", "Matemáticas",
                "Cálculo", "nuevo@uni.edu", "123", 3, "img.jpg");
        when(docenteRepository.save(nuevo)).thenReturn(new Docente(3L, "Nuevo Profesor",
                "Matemáticas", "Cálculo", "nuevo@uni.edu", "123", 3, "img.jpg"));

        // When
        Docente guardado = docenteService.guardar(nuevo);

        // Then
        assertNotNull(guardado.getId());
        assertEquals(3L, guardado.getId());
        verify(docenteRepository).save(nuevo);
    }

    @Test
    void buscar_DebeBuscarPorNombreYArea() {
        // Given
        when(docenteRepository.findByNombreContainingIgnoreCase("Miguel"))
                .thenReturn(Arrays.asList(docente1));
        when(docenteRepository.findByAreaContainingIgnoreCase("Miguel"))
                .thenReturn(Arrays.asList());

        // When
        List<Docente> resultado = docenteService.buscar("Miguel");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Miguel Madarriaga", resultado.get(0).getNombre());
    }
}