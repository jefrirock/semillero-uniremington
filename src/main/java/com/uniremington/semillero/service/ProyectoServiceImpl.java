package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Proyecto;
import com.uniremington.semillero.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Autowired
    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public List<Proyecto> listarTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public List<Proyecto> listarPorArea(String area) {
        return proyectoRepository.findByArea(area);
    }

    @Override
    public List<Proyecto> listarPorEstado(String estado) {
        return proyectoRepository.findByEstado(estado);
    }

    @Override
    public Optional<Proyecto> buscarPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminar(Long id) {
        proyectoRepository.deleteById(id);
    }
}