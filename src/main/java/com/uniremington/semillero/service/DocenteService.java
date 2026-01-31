package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Docente;
import java.util.List;
import java.util.Optional;

public interface DocenteService {
    List<Docente> listarTodos();
    Optional<Docente> buscarPorId(Long id);
    List<Docente> buscarPorArea(String area);
    Docente guardar(Docente docente);
    void eliminar(Long id);
    List<Docente> buscar(String termino);
}