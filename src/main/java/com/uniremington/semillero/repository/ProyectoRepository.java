package com.uniremington.semillero.repository;

import com.uniremington.semillero.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByArea(String area);
    List<Proyecto> findByEstado(String estado);
    List<Proyecto> findByAreaAndEstado(String area, String estado);
}