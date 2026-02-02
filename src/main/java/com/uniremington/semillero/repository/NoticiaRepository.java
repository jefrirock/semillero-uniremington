package com.uniremington.semillero.repository;

import com.uniremington.semillero.model.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long> {
    List<Noticia> findByActivaTrueOrderByOrdenAsc();
}