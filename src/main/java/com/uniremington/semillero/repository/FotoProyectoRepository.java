package com.uniremington.semillero.repository;

import com.uniremington.semillero.model.FotoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoProyectoRepository extends JpaRepository<FotoProyecto, Long> {
    List<FotoProyecto> findByProyectoId(Long proyectoId);
}