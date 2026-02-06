package com.uniremington.semillero.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    private String area; // Ingeniería, Salud, Sociales, Ambiental, etc.

    private String estado; // Activo, Finalizado, En progreso

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String imagenPrincipal; // Foto del proyecto

    private String integrantes; // Nombres separados por coma

    private String fotosIntegrantes; // URLs de fotos separadas por coma

    private String objetivos;

    private String resultados;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoProyecto> fotos = new ArrayList<>();

    public Proyecto() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getImagenPrincipal() { return imagenPrincipal; }
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }

    public String getIntegrantes() { return integrantes; }
    public void setIntegrantes(String integrantes) { this.integrantes = integrantes; }

    public String getFotosIntegrantes() { return fotosIntegrantes; }
    public void setFotosIntegrantes(String fotosIntegrantes) { this.fotosIntegrantes = fotosIntegrantes; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getResultados() { return resultados; }
    public void setResultados(String resultados) { this.resultados = resultados; }

    public List<FotoProyecto> getFotos() { return fotos; }
    public void setFotos(List<FotoProyecto> fotos) { this.fotos = fotos; }
}