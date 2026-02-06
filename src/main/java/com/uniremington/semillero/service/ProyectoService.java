package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Proyecto;
import java.util.List;
import java.util.Optional;

public interface ProyectoService {
    List<Proyecto> listarTodos();
    List<Proyecto> listarPorArea(String area);
    List<Proyecto> listarPorEstado(String estado);
    Optional<Proyecto> buscarPorId(Long id);
    Proyecto guardar(Proyecto proyecto);
    void eliminar(Long id);
}