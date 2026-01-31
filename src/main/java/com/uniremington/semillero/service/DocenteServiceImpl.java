package com.uniremington.semillero.service;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;

    @Autowired
    public DocenteServiceImpl(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    @Override
    public List<Docente> listarTodos() {
        return docenteRepository.findAll();
    }

    @Override
    public Optional<Docente> buscarPorId(Long id) {
        return docenteRepository.findById(id);
    }

    @Override
    public List<Docente> buscarPorArea(String area) {
        return docenteRepository.findByAreaContainingIgnoreCase(area);
    }

    @Override
    public Docente guardar(Docente docente) {
        return docenteRepository.save(docente);
    }

    @Override
    public void eliminar(Long id) {
        docenteRepository.deleteById(id);
    }

    @Override
    public List<Docente> buscar(String termino) {
        List<Docente> porNombre = docenteRepository.findByNombreContainingIgnoreCase(termino);
        List<Docente> porArea = docenteRepository.findByAreaContainingIgnoreCase(termino);
        porNombre.addAll(porArea);
        return porNombre.stream().distinct().toList();
    }
}