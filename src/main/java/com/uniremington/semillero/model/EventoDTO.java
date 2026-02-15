package com.uniremington.semillero.model;

import java.time.LocalDate;
import java.util.List;

public class EventoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String lugar;
    private LocalDate fecha;
    private String horaInicio;
    private String horaFin;
    private Long organizadorId;
    private String imagenUrl;
    private List<FotoEventoDTO> fotos;

    // Constructor vacío
    public EventoDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public Long getOrganizadorId() { return organizadorId; }
    public void setOrganizadorId(Long organizadorId) { this.organizadorId = organizadorId; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public List<FotoEventoDTO> getFotos() { return fotos; }
    public void setFotos(List<FotoEventoDTO> fotos) { this.fotos = fotos; }
}