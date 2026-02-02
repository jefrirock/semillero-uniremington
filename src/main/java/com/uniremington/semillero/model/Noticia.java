package com.uniremington.semillero.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "noticias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String subtitulo;
    private String imagenUrl;
    private String link;

    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    private Boolean activa = true;
    private Integer orden = 0; // Para ordenar en el carrusel
}