package com.uniremington.semillero.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "noticias")
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String subtitulo;
    private String imagenUrl;
    private String link;

    // SOLO UNA DECLARACIÓN - Sin @Column(nullable = false)
    private LocalDate fechaPublicacion;

    private Boolean activa = true;
    private Integer orden = 0;

    public Noticia() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}