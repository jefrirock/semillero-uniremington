package com.uniremington.semillero.model;

import jakarta.persistence.*;

@Entity
@Table(name = "docentes")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String area;
    private String especialidad;
    private String email;
    private String telefono;
    private Integer experienciaAnios;
    private String imagenUrl;

    private Integer orden = 0; // NUEVO: Campo para orden personalizado

    public Docente() {}

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

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    // NUEVO
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}