package com.uniremington.semillero.controller;

import com.uniremington.semillero.model.DocenteDTO;
import com.uniremington.semillero.model.EventoDTO;
import com.uniremington.semillero.model.FotoEventoDTO;
import com.uniremington.semillero.service.DocenteClientService;
import com.uniremington.semillero.service.EventoClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private DocenteClientService docenteClientService;

    @Autowired
    private EventoClientService eventoClientService;

    // ==================== NOTICIAS ESTÁTICAS ====================
    public static class NoticiaEstatica {
        private String titulo;
        private String subtitulo;
        private String imagenUrl;
        private String link;

        public NoticiaEstatica(String titulo, String subtitulo, String imagenUrl, String link) {
            this.titulo = titulo;
            this.subtitulo = subtitulo;
            this.imagenUrl = imagenUrl;
            this.link = link;
        }

        public String getTitulo() { return titulo; }
        public String getSubtitulo() { return subtitulo; }
        public String getImagenUrl() { return imagenUrl; }
        public String getLink() { return link; }
    }

    // ==================== INDEX ====================
    @GetMapping("/")
    public String index(Model model) {
        List<NoticiaEstatica> noticias = Arrays.asList(
                new NoticiaEstatica("Nuevo Semillero de Investigación",
                        "Participa en nuestros proyectos de innovación y desarrollo tecnológico",
                        "/images/noticia1.jpg", "/semillero"),
                new NoticiaEstatica("Convocatoria Docente 2026",
                        "Únete a nuestro equipo de profesionales calificados",
                        "/images/noticia2.jpg", "/docentes"),
                new NoticiaEstatica("Eventos Académicos",
                        "Conoce nuestros próximos eventos y actividades",
                        "/images/noticia3.jpg", "/eventos"),
                new NoticiaEstatica("Proyectos Destacados",
                        "Descubre los proyectos más innovadores de nuestros estudiantes",
                        "/images/noticia4.jpg", "/semillero")
        );
        model.addAttribute("noticias", noticias);

        try {
            List<EventoDTO> eventos = eventoClientService.listarProximos();
            model.addAttribute("eventos", eventos);
        } catch (Exception e) {
            model.addAttribute("eventos", Collections.emptyList());
        }

        return "index";
    }

    // ==================== DOCENTES ====================
    @GetMapping("/docentes")
    public String docentes(@RequestParam(required = false) String area, Model model) {
        List<DocenteDTO> docentes;
        String mensajeError = null;

        System.out.println(">>> HOME CONTROLLER: Cargando docentes, área: " + area);

        try {
            if (area != null && !area.isEmpty()) {
                docentes = docenteClientService.listarPorArea(area);
            } else {
                docentes = docenteClientService.listarTodos();
            }

            System.out.println(">>> HOME CONTROLLER: Docentes recibidos: " + (docentes != null ? docentes.size() : "null"));

            if (docentes != null && !docentes.isEmpty()) {
                docentes.sort((d1, d2) -> {
                    Integer o1 = d1.getOrden() != null ? d1.getOrden() : 0;
                    Integer o2 = d2.getOrden() != null ? d2.getOrden() : 0;
                    return o1.compareTo(o2);
                });
            }

        } catch (Exception e) {
            System.err.println(">>> HOME CONTROLLER: Error cargando docentes: " + e.getMessage());
            docentes = Collections.emptyList();
            mensajeError = "No se pudo conectar con el servicio de docentes. Verifique que ms-docentes esté ejecutándose en puerto 8081.";
        }

        model.addAttribute("docentes", docentes != null ? docentes : Collections.emptyList());
        model.addAttribute("areaSeleccionada", area);
        model.addAttribute("areas", Arrays.asList("Ingenieria", "Salud", "Administracion", "Educacion", "Derecho", "Contaduria"));
        model.addAttribute("errorConexion", mensajeError);

        return "docentes";
    }

    @GetMapping("/docentes/nuevo")
    public String formularioDocente(Model model) {
        model.addAttribute("docente", new DocenteDTO());
        model.addAttribute("areas", Arrays.asList("Ingenieria", "Salud", "Administracion", "Educacion", "Derecho", "Contaduria"));
        return "formulario-docente";
    }

    @PostMapping("/docentes/guardar")
    public String guardarDocente(@ModelAttribute DocenteDTO docente,
                                 @RequestParam(value = "foto", required = false) MultipartFile foto,
                                 Model model) {
        System.out.println(">>> HOME CONTROLLER: Guardando docente: " + docente.getNombre());
        System.out.println(">>> HOME CONTROLLER: Área: " + docente.getArea());
        System.out.println(">>> HOME CONTROLLER: Orden: " + docente.getOrden());
        System.out.println(">>> HOME CONTROLLER: Email: " + docente.getEmail());
        System.out.println(">>> HOME CONTROLLER: Teléfono: " + docente.getTelefono());

        try {
            DocenteDTO guardado = docenteClientService.guardar(docente, foto);
            if (guardado != null && guardado.getId() != null) {
                System.out.println(">>> HOME CONTROLLER: Docente guardado con ID: " + guardado.getId());
                return "redirect:/docentes?exito=true";
            } else {
                System.err.println(">>> HOME CONTROLLER: Error - El servicio devolvió null o sin ID");
                model.addAttribute("error", "Error al guardar: El servicio no respondió correctamente. Verifique que ms-docentes esté ejecutándose en puerto 8081.");
                model.addAttribute("docente", docente);
                model.addAttribute("areas", Arrays.asList("Ingenieria", "Salud", "Administracion", "Educacion", "Derecho", "Contaduria"));
                return "formulario-docente";
            }
        } catch (Exception e) {
            System.err.println(">>> HOME CONTROLLER: Error al guardar docente: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar docente: " + e.getMessage());
            model.addAttribute("docente", docente);
            model.addAttribute("areas", Arrays.asList("Ingenieria", "Salud", "Administracion", "Educacion", "Derecho", "Contaduria"));
            return "formulario-docente";
        }
    }

    // ==================== ELIMINAR DOCENTE CON LOGIN ====================
    @GetMapping("/docentes/eliminar/{id}")
    public String mostrarLoginEliminarDocente(@PathVariable Long id, Model model) {
        model.addAttribute("docenteId", id);
        return "login-eliminar-docente";
    }

    @PostMapping("/docentes/eliminar/{id}")
    public String eliminarDocente(@PathVariable Long id,
                                  @RequestParam("usuario") String usuario,
                                  @RequestParam("password") String password,
                                  Model model) {
        // Validar credenciales
        if (!"Admin".equals(usuario) || !"2026".equals(password)) {
            model.addAttribute("docenteId", id);
            model.addAttribute("error", "❌ Usuario o contraseña incorrectos");
            return "login-eliminar-docente";
        }

        try {
            System.out.println(">>> HOME CONTROLLER: Eliminando docente ID: " + id);
            docenteClientService.eliminar(id);
            System.out.println(">>> HOME CONTROLLER: Docente eliminado exitosamente");
            return "redirect:/docentes?eliminado=true";
        } catch (Exception e) {
            System.err.println(">>> HOME CONTROLLER: Error eliminando docente: " + e.getMessage());
            model.addAttribute("docenteId", id);
            model.addAttribute("error", "Error al eliminar: " + e.getMessage());
            return "login-eliminar-docente";
        }
    }

    // ==================== EVENTOS ====================
    @GetMapping("/eventos")
    public String eventos(@RequestParam(required = false) String categoria, Model model) {
        List<EventoDTO> eventos;
        String mensajeError = null;

        try {
            if (categoria != null && !categoria.isEmpty()) {
                eventos = eventoClientService.listarPorCategoria(categoria);
            } else {
                eventos = eventoClientService.listarTodos();
            }
        } catch (Exception e) {
            System.err.println("Error cargando eventos: " + e.getMessage());
            eventos = Collections.emptyList();
            mensajeError = "No se pudo conectar con el servicio de eventos. Verifique que ms-eventos esté ejecutándose en puerto 8082.";
        }

        model.addAttribute("eventos", eventos != null ? eventos : Collections.emptyList());
        model.addAttribute("categoriaActual", categoria);
        model.addAttribute("categorias", Arrays.asList("Academico", "Cultural", "Deportivo", "Investigacion", "Extension"));
        model.addAttribute("errorConexion", mensajeError);

        return "eventos";
    }

    @GetMapping("/eventos/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        try {
            eventoClientService.eliminar(id);
            return "redirect:/eventos?eliminado=true";
        } catch (Exception e) {
            System.err.println("Error eliminando evento: " + e.getMessage());
            return "redirect:/eventos?error=true";
        }
    }

    @GetMapping("/eventos/nuevo")
    public String formularioEvento(Model model) {
        try {
            List<DocenteDTO> docentes = docenteClientService.listarTodos();
            model.addAttribute("docentes", docentes);
        } catch (Exception e) {
            model.addAttribute("docentes", Collections.emptyList());
            model.addAttribute("errorDocentes", "No se pudieron cargar los docentes");
        }
        model.addAttribute("evento", new EventoDTO());
        model.addAttribute("categorias", Arrays.asList("Academico", "Cultural", "Deportivo", "Investigacion", "Extension"));
        return "formulario-evento";
    }

    @PostMapping("/eventos/guardar")
    public String guardarEvento(@ModelAttribute EventoDTO evento,
                                @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                Model model) {
        try {
            eventoClientService.guardar(evento, imagen);
            return "redirect:/eventos?exito=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar evento: " + e.getMessage());
            model.addAttribute("evento", evento);
            model.addAttribute("categorias", Arrays.asList("Academico", "Cultural", "Deportivo", "Investigacion", "Extension"));
            try {
                model.addAttribute("docentes", docenteClientService.listarTodos());
            } catch (Exception ex) {
                model.addAttribute("docentes", Collections.emptyList());
            }
            return "formulario-evento";
        }
    }

    @GetMapping("/eventos/{id}")
    public String detalleEvento(@PathVariable Long id, Model model) {
        try {
            EventoDTO evento = eventoClientService.buscarPorId(id);
            if (evento == null) {
                return "redirect:/eventos?error=notfound";
            }
            model.addAttribute("evento", evento);

            try {
                List<FotoEventoDTO> fotos = eventoClientService.listarFotosPorEvento(id);
                model.addAttribute("fotos", fotos);
                model.addAttribute("cantidadFotos", fotos != null ? fotos.size() : 0); // <-- AGREGAR ESTA LÍNEA
            } catch (Exception e) {
                model.addAttribute("fotos", Collections.emptyList());
                model.addAttribute("cantidadFotos", 0); // <-- Y ESTA
            }

            return "detalle-evento";
        } catch (Exception e) {
            return "redirect:/eventos?error=true";
        }
    }

    // ==================== AGREGAR FOTOS A EVENTO ====================

    @PostMapping("/eventos/{id}/fotos")
    public String agregarFotosEvento(@PathVariable Long id,
                                     @RequestParam("fotos") List<MultipartFile> fotos,
                                     Model model) {
        try {
            System.out.println(">>> HOME CONTROLLER: Agregando " + fotos.size() + " fotos al evento " + id);
            eventoClientService.agregarFotos(id, fotos);
            return "redirect:/eventos/" + id + "?fotosAgregadas=true";
        } catch (Exception e) {
            System.err.println(">>> HOME CONTROLLER: Error agregando fotos: " + e.getMessage());
            return "redirect:/eventos/" + id + "?error=true";
        }
    }

    @GetMapping("/eventos/fotos/eliminar/{id}")
    public String eliminarFoto(@PathVariable Long id,
                               @RequestParam("eventoId") Long eventoId) {
        try {
            System.out.println(">>> HOME CONTROLLER: Eliminando foto " + id + " del evento " + eventoId);
            // Aquí necesitarías agregar el método en EventoClientService si quieres eliminar fotos
            return "redirect:/eventos/" + eventoId + "?fotoEliminada=true";
        } catch (Exception e) {
            return "redirect:/eventos/" + eventoId + "?error=true";
        }
    }

    // ==================== EDITAR EVENTO ====================
    @GetMapping("/eventos/editar/{id}")
    public String formularioEditarEvento(@PathVariable Long id, Model model) {
        try {
            EventoDTO evento = eventoClientService.buscarPorId(id);
            if (evento == null) {
                return "redirect:/eventos?error=notfound";
            }

            try {
                List<DocenteDTO> docentes = docenteClientService.listarTodos();
                model.addAttribute("docentes", docentes);
            } catch (Exception e) {
                model.addAttribute("docentes", Collections.emptyList());
            }

            model.addAttribute("evento", evento);
            model.addAttribute("categorias", Arrays.asList("Academico", "Cultural", "Deportivo", "Investigacion", "Extension"));
            model.addAttribute("modoEdicion", true); // Para saber que es edición

            return "formulario-evento";
        } catch (Exception e) {
            return "redirect:/eventos?error=true";
        }
    }

    // ==================== SEMILLERO ====================
    @GetMapping("/semillero")
    public String semillero(Model model) {
        return "semillero";
    }

    // ==================== PROGRAMAS ====================
    @GetMapping("/programas")
    public String programas(Model model) {
        return "programas";
    }

    // ==================== CONTACTO ====================
    @GetMapping("/contacto")
    public String contacto(Model model) {
        return "contacto";
    }
}