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
        // CAMBIO: Usar nuevo método ordenado
        return docenteRepository.findAllByOrderByOrdenAsc();
    }

    @Override
    public Optional<Docente> buscarPorId(Long id) {
        return docenteRepository.findById(id);
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
    public List<Docente> buscar(String query) {
        // CAMBIO: Usar nuevo método ordenado
        return docenteRepository.findByNombreContainingIgnoreCaseOrderByOrdenAsc(query);
    }
}