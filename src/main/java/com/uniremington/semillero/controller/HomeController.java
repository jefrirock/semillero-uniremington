package com.uniremington.semillero.controller;

import com.uniremington.semillero.model.Docente;
import com.uniremington.semillero.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final DocenteService docenteService;

    @Autowired
    public HomeController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    // Página de inicio
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("docentes", docenteService.listarTodos());
        return "index";
    }

    // Página de listado de docentes
    @GetMapping("/docentes")
    public String docentes(@RequestParam(required = false) String busqueda, Model model) {
        if (busqueda != null && !busqueda.isEmpty()) {
            model.addAttribute("docentes", docenteService.buscar(busqueda));
        } else {
            model.addAttribute("docentes", docenteService.listarTodos());
        }
        return "docentes";
    }

    // Formulario para agregar docente
    @GetMapping("/docentes/nuevo")
    public String formularioDocente(Model model) {
        model.addAttribute("docente", new Docente());
        return "formulario-docente";
    }

    // Guardar docente (cuando envían el formulario)
    @PostMapping("/docentes/guardar")
    public String guardarDocente(@ModelAttribute Docente docente) {
        docenteService.guardar(docente);
        return "redirect:/docentes";
    }
}