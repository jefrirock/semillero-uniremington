package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoService {
    List<Evento> listarTodos();
    Optional<Evento> buscarPorId(Long id);
    List<Evento> listarProximos();  // Eventos futuros
    Evento guardar(Evento evento);
    void eliminar(Long id);
    List<Evento> listarPorCategoria(String categoria);
}