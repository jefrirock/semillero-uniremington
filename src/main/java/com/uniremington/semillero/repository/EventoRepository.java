package com.uniremington.semillero.repository;

import com.uniremington.semillero.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByCategoria(String categoria);
    List<Evento> findByFechaAfter(LocalDate fecha);
    List<Evento> findByFechaAfterOrderByFechaDesc(LocalDate fecha);
}