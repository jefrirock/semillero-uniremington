package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Evento;
import com.uniremington.semillero.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;

    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    @Override
    public Optional<Evento> buscarPorId(Long id) {
        return eventoRepository.findById(id);
    }

    @Override
    public List<Evento> listarProximos() {
        return eventoRepository.findByFechaAfter(LocalDate.now());
    }

    @Override
    public Evento guardar(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Override
    public void eliminar(Long id) {
        eventoRepository.deleteById(id);
    }
}