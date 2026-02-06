package com.uniremington.semillero.controller;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.model.Evento;
import com.uniremington.semillero.model.FotoEvento;
import com.uniremington.semillero.model.Noticia;
import com.uniremington.semillero.service.DocenteService;
import com.uniremington.semillero.service.EventoService;
import com.uniremington.semillero.repository.FotoEventoRepository;
import com.uniremington.semillero.repository.NoticiaRepository;
import com.uniremington.semillero.model.Proyecto;
import com.uniremington.semillero.model.FotoProyecto;
import com.uniremington.semillero.service.ProyectoService;
import com.uniremington.semillero.repository.FotoProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    private final DocenteService docenteService;
    private final EventoService eventoService;

    @Autowired
    private FotoEventoRepository fotoEventoRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private FotoProyectoRepository fotoProyectoRepository;



    @Autowired
    public HomeController(DocenteService docenteService, EventoService eventoService) {
        this.docenteService = docenteService;
        this.eventoService = eventoService;
    }

    // Página de inicio con carrusel de noticias
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("docentes", docenteService.listarTodos());
        model.addAttribute("eventos", eventoService.listarProximos());
        model.addAttribute("noticias", noticiaRepository.findByActivaTrueOrderByOrdenAsc());
        return "index";
    }

    // Página de docentes con búsqueda
    @GetMapping("/docentes")
    public String docentes(@RequestParam(required = false) String busqueda, Model model) {
        if (busqueda != null && !busqueda.isEmpty()) {
            model.addAttribute("docentes", docenteService.buscar(busqueda));
        } else {
            model.addAttribute("docentes", docenteService.listarTodos());
        }
        return "docentes";
    }

    // Formulario nuevo docente
    @GetMapping("/docentes/nuevo")
    public String formularioDocente(Model model) {
        model.addAttribute("docente", new Docente());
        return "formulario-docente";
    }

    // Guardar docente con foto
    @PostMapping("/docentes/guardar")
    public String guardarDocente(@ModelAttribute Docente docente,
                                 @RequestParam("foto") MultipartFile foto) {
        try {
            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (!foto.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
                java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(foto.getInputStream(), filePath);
                docente.setImagenUrl(fileName);
            }

            docenteService.guardar(docente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/docentes";
    }

    // Página de Programas
    @GetMapping("/programas")
    public String programas() {
        return "programas";
    }

    // Página de Contacto
    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    // ✅ EVENTOS CON FILTRO
    @GetMapping("/eventos")
    public String eventos(@RequestParam(required = false) String categoria, Model model) {
        List<Evento> eventos;

        if (categoria != null && !categoria.isEmpty()) {
            eventos = eventoService.listarPorCategoria(categoria);
            model.addAttribute("categoriaActual", categoria);
        } else {
            eventos = eventoService.listarProximos();
            model.addAttribute("categoriaActual", null);
        }

        model.addAttribute("eventos", eventos);
        return "eventos";
    }

    // Formulario crear evento
    @GetMapping("/eventos/nuevo")
    public String formularioEvento(Model model) {
        model.addAttribute("evento", new Evento());
        return "formulario-evento";
    }

    // Guardar evento
    @PostMapping("/eventos/guardar")
    public String guardarEvento(@ModelAttribute Evento evento,
                                @RequestParam("imagen") MultipartFile imagen) {
        try {
            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (!imagen.isEmpty()) {
                String fileName = "evento_" + System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(imagen.getInputStream(), filePath);
                evento.setImagenUrl(fileName);
            }

            eventoService.guardar(evento);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/eventos/" + evento.getId();
    }

    // ✅ CRUD - EDITAR EVENTO
    @GetMapping("/eventos/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model) {
        Evento evento = eventoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        model.addAttribute("evento", evento);
        model.addAttribute("editando", true);
        return "formulario-evento";
    }

    // ✅ CRUD - ACTUALIZAR EVENTO
    @PostMapping("/eventos/actualizar")
    public String actualizarEvento(@ModelAttribute Evento evento,
                                   @RequestParam("imagen") MultipartFile imagen) {
        try {
            if (!imagen.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = "evento_" + System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(imagen.getInputStream(), filePath);
                evento.setImagenUrl(fileName);
            }
            eventoService.guardar(evento);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/eventos/" + evento.getId();
    }

    // ✅ CRUD - ELIMINAR EVENTO
    @GetMapping("/eventos/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        List<FotoEvento> fotos = fotoEventoRepository.findByEventoId(id);
        fotoEventoRepository.deleteAll(fotos);
        eventoService.eliminar(id);
        return "redirect:/eventos";
    }

    // ✅ DETALLE EVENTO (Galería de fotos)
    @GetMapping("/eventos/{id}")
    public String detalleEvento(@PathVariable Long id, Model model) {
        Evento evento = eventoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        List<FotoEvento> fotos = fotoEventoRepository.findByEventoId(id);

        model.addAttribute("evento", evento);
        model.addAttribute("fotos", fotos);
        model.addAttribute("cantidadFotos", fotos.size());
        return "detalle-evento";
    }

    // ✅ AGREGAR FOTOS AL EVENTO
    @PostMapping("/eventos/{id}/fotos")
    public String agregarFotosEvento(@PathVariable Long id,
                                     @RequestParam("fotos") MultipartFile[] fotos) {
        try {
            Evento evento = eventoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            for (MultipartFile foto : fotos) {
                if (!foto.isEmpty()) {
                    String fileName = "evento_" + id + "_" + System.currentTimeMillis() + "_" + foto.getOriginalFilename();
                    java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                    java.nio.file.Files.copy(foto.getInputStream(), filePath);

                    FotoEvento fotoEvento = new FotoEvento();
                    fotoEvento.setNombreArchivo(fileName);
                    fotoEvento.setEvento(evento);
                    fotoEventoRepository.save(fotoEvento);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/eventos/" + id;
    }

    // ✅ ELIMINAR FOTO INDIVIDUAL
    @GetMapping("/eventos/fotos/eliminar/{fotoId}")
    public String eliminarFoto(@PathVariable Long fotoId) {
        FotoEvento foto = fotoEventoRepository.findById(fotoId)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada"));
        Long eventoId = foto.getEvento().getId();
        fotoEventoRepository.deleteById(fotoId);
        return "redirect:/eventos/" + eventoId;
    }

    // ✅ ADMIN NOTICIAS (para el carrusel)
    @GetMapping("/admin/noticias")
    public String adminNoticias(Model model) {
        model.addAttribute("noticias", noticiaRepository.findAll());
        model.addAttribute("noticia", new Noticia());
        return "admin-noticias";
    }

    // ✅ GUARDAR NOTICIA
    @PostMapping("/admin/noticias/guardar")
    public String guardarNoticia(@ModelAttribute Noticia noticia,
                                 @RequestParam("imagen") MultipartFile imagen) {
        try {
            if (!imagen.isEmpty()) {
                String uploadDir = "uploads/";
                java.io.File dir = new java.io.File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = "noticia_" + System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(imagen.getInputStream(), filePath);
                noticia.setImagenUrl(fileName);
            }
            if (noticia.getFechaPublicacion() == null) {
                noticia.setFechaPublicacion(LocalDate.now());
            }
            noticiaRepository.save(noticia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/noticias";
    }

    // ✅ ELIMINAR NOTICIA
    @GetMapping("/admin/noticias/eliminar/{id}")
    public String eliminarNoticia(@PathVariable Long id) {
        noticiaRepository.deleteById(id);
        return "redirect:/admin/noticias";
    }

    // ==================== SEMILLERO DE INVESTIGACIÓN ====================

    // Página pública del semillero
    @GetMapping("/semillero")
    public String semillero(@RequestParam(required = false) String area,
                            @RequestParam(required = false) String estado,
                            Model model) {
        List<Proyecto> proyectos;

        if (area != null && !area.isEmpty() && estado != null && !estado.isEmpty()) {
            proyectos = proyectoService.listarPorArea(area);
            // Filtrar por estado manualmente ya que el método combinado puede no existir
            proyectos.removeIf(p -> !p.getEstado().equals(estado));
            model.addAttribute("areaActual", area);
            model.addAttribute("estadoActual", estado);
        } else if (area != null && !area.isEmpty()) {
            proyectos = proyectoService.listarPorArea(area);
            model.addAttribute("areaActual", area);
            model.addAttribute("estadoActual", null);
        } else if (estado != null && !estado.isEmpty()) {
            proyectos = proyectoService.listarPorEstado(estado);
            model.addAttribute("areaActual", null);
            model.addAttribute("estadoActual", estado);
        } else {
            proyectos = proyectoService.listarTodos();
            model.addAttribute("areaActual", null);
            model.addAttribute("estadoActual", null);
        }

        model.addAttribute("proyectos", proyectos);
        return "semillero";
    }

    // Formulario nuevo proyecto (admin)
    @GetMapping("/semillero/nuevo")
    public String formularioProyecto(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "formulario-proyecto";
    }

    // Guardar proyecto con imagen principal
    @PostMapping("/semillero/guardar")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto,
                                  @RequestParam("imagenPrincipal") MultipartFile imagenPrincipal,
                                  @RequestParam(value = "fotosIntegrantes", required = false) MultipartFile[] fotosIntegrantes) {
        try {
            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Guardar imagen principal
            if (!imagenPrincipal.isEmpty()) {
                String fileName = "proyecto_" + System.currentTimeMillis() + "_" + imagenPrincipal.getOriginalFilename();
                java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(imagenPrincipal.getInputStream(), filePath);
                proyecto.setImagenPrincipal(fileName);
            }

            // Guardar fotos de integrantes (concatenadas)
            if (fotosIntegrantes != null && fotosIntegrantes.length > 0) {
                StringBuilder fotosInt = new StringBuilder();
                for (int i = 0; i < fotosIntegrantes.length; i++) {
                    MultipartFile foto = fotosIntegrantes[i];
                    if (!foto.isEmpty()) {
                        String fileName = "integrante_" + System.currentTimeMillis() + "_" + i + "_" + foto.getOriginalFilename();
                        java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                        java.nio.file.Files.copy(foto.getInputStream(), filePath);
                        if (fotosInt.length() > 0) fotosInt.append(",");
                        fotosInt.append(fileName);
                    }
                }
                proyecto.setFotosIntegrantes(fotosInt.toString());
            }

            if (proyecto.getFechaInicio() == null) {
                proyecto.setFechaInicio(LocalDate.now());
            }

            proyectoService.guardar(proyecto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/semillero/" + proyecto.getId();
    }

    // Detalle del proyecto con galería
    @GetMapping("/semillero/{id}")
    public String detalleProyecto(@PathVariable Long id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        List<FotoProyecto> fotos = fotoProyectoRepository.findByProyectoId(id);

        model.addAttribute("proyecto", proyecto);
        model.addAttribute("fotos", fotos);
        model.addAttribute("cantidadFotos", fotos.size());

        // Parsear integrantes y fotos
        String[] listaIntegrantes = proyecto.getIntegrantes() != null ?
                proyecto.getIntegrantes().split(",") : new String[0];
        String[] listaFotosInt = proyecto.getFotosIntegrantes() != null ?
                proyecto.getFotosIntegrantes().split(",") : new String[0];

        model.addAttribute("listaIntegrantes", listaIntegrantes);
        model.addAttribute("listaFotosInt", listaFotosInt);

        return "detalle-proyecto";
    }

    // Agregar fotos al proyecto
    @PostMapping("/semillero/{id}/fotos")
    public String agregarFotosProyecto(@PathVariable Long id,
                                       @RequestParam("fotos") MultipartFile[] fotos) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            for (MultipartFile foto : fotos) {
                if (!foto.isEmpty()) {
                    String fileName = "proyecto_" + id + "_" + System.currentTimeMillis() + "_" + foto.getOriginalFilename();
                    java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
                    java.nio.file.Files.copy(foto.getInputStream(), filePath);

                    FotoProyecto fotoProyecto = new FotoProyecto();
                    fotoProyecto.setNombreArchivo(fileName);
                    fotoProyecto.setProyecto(proyecto);
                    fotoProyectoRepository.save(fotoProyecto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/semillero/" + id;
    }

    // Eliminar foto del proyecto
    @GetMapping("/semillero/fotos/eliminar/{fotoId}")
    public String eliminarFotoProyecto(@PathVariable Long fotoId) {
        FotoProyecto foto = fotoProyectoRepository.findById(fotoId)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada"));
        Long proyectoId = foto.getProyecto().getId();
        fotoProyectoRepository.deleteById(fotoId);
        return "redirect:/semillero/" + proyectoId;
    }

    // Eliminar proyecto completo
    @GetMapping("/semillero/eliminar/{id}")
    public String eliminarProyecto(@PathVariable Long id) {
        List<FotoProyecto> fotos = fotoProyectoRepository.findByProyectoId(id);
        fotoProyectoRepository.deleteAll(fotos);
        proyectoService.eliminar(id);
        return "redirect:/semillero";
    }



}