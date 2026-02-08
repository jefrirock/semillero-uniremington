package com.uniremington.semillero.repository;

import com.uniremington.semillero.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    // NUEVO: Listar todos ordenados por campo "orden"
    List<Docente> findAllByOrderByOrdenAsc();

    // NUEVO: Buscar por nombre ordenados
    List<Docente> findByNombreContainingIgnoreCaseOrderByOrdenAsc(String nombre);

    // NUEVO: Buscar por área ordenados
    List<Docente> findByAreaContainingIgnoreCaseOrderByOrdenAsc(String area);
}