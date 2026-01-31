package com.uniremington.semillero.repository;

import com.uniremington.semillero.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    List<Docente> findByAreaContainingIgnoreCase(String area);
    List<Docente> findByNombreContainingIgnoreCase(String nombre);
}