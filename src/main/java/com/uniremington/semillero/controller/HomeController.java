package com.uniremington.semillero.controller;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.model.Evento;
import com.uniremington.semillero.model.FotoEvento;
import com.uniremington.semillero.service.DocenteService;
import com.uniremington.semillero.service.EventoService;
import com.uniremington.semillero.repository.FotoEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class HomeController {

    private final DocenteService docenteService;
    private final EventoService eventoService;

    @Autowired
    private FotoEventoRepository fotoEventoRepository;

    @Autowired
    public HomeController(DocenteService docenteService, EventoService eventoService) {
        this.docenteService = docenteService;
        this.eventoService = eventoService;
    }

    // Página de inicio
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("docentes", docenteService.listarTodos());
        model.addAttribute("eventos", eventoService.listarProximos());
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
        return "redirect:/eventos/" + evento.getId(); // Redirige al detalle para agregar fotos
    }

    // ✅ CRUD - EDITAR
    @GetMapping("/eventos/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model) {
        Evento evento = eventoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        model.addAttribute("evento", evento);
        model.addAttribute("editando", true);
        return "formulario-evento";
    }

    // ✅ CRUD - ACTUALIZAR
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

    // ✅ CRUD - ELIMINAR
    @GetMapping("/eventos/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        // Primero eliminar fotos asociadas
        List<FotoEvento> fotos = fotoEventoRepository.findByEventoId(id);
        fotoEventoRepository.deleteAll(fotos);
        // Luego eliminar evento
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
}