package com.uniremington.semillero.controller;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.service.DocenteService;
import com.uniremington.semillero.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final DocenteService docenteService;
    private final EventoService eventoService;

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

    // Página de docentes
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

    // Guardar docente
    @PostMapping("/docentes/guardar")
    public String guardarDocente(@ModelAttribute Docente docente) {
        docenteService.guardar(docente);
        return "redirect:/docentes";
    }

    // ✅ NUEVO: Página de Programas
    @GetMapping("/programas")
    public String programas() {
        return "programas";
    }

    // ✅ NUEVO: Página de Eventos
    @GetMapping("/eventos")
    public String eventos(Model model) {
        model.addAttribute("eventos", eventoService.listarProximos());
        return "eventos";
    }

    // ✅ NUEVO: Página de Contacto
    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }
}