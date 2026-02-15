package com.uniremington.semillero.model;

public class DocenteDTO {
    private Long id;
    private String nombre;
    private String area;
    private String especialidad;
    private String email;
    private String telefono;
    private Integer experienciaAnios;
    private String fotoUrl;
    private Integer orden; // NUEVO CAMPO

    // Constructor vacío
    public DocenteDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getExperienciaAnios() { return experienciaAnios; }
    public void setExperienciaAnios(Integer experienciaAnios) { this.experienciaAnios = experienciaAnios; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}