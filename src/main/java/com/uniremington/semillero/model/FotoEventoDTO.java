package com.uniremington.semillero.model;

public class FotoEventoDTO {
    private Long id;
    private String url;
    private Long eventoId;

    // Constructor vacío
    public FotoEventoDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
}