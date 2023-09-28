package Pep1.controllers;

import Pep1.entities.EstudiantesEntity;
import Pep1.services.EstudiantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@RequestMapping
public class EstudiantesController {

    @Autowired
    private EstudiantesService service;

    @GetMapping("/lista-estudiantes")
    public String listar(Model model) {
        ArrayList<EstudiantesEntity> estudiantes = service.obtenerEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "index";
    }

    @GetMapping("/nuevo-estudiante")
    public String estudiante(){
        return "nuevo-estudiante";
    }
    @PostMapping("/nuevo-estudiante")
    public String nuevoEstudiante(@RequestParam("Nombres") String nombreEstudiante,
                                 @RequestParam("Apellido") String apellidoEstudiante,
                                 @RequestParam("Rut") String rutEstudiante,
                                 @RequestParam("Fecha NAcimiento") String fechaNacimiento,
                                 @RequestParam("Tipo Colegio") String tipoColegio,
                                 @RequestParam("Nombre Colegio") String nombreColegio,
                                 @RequestParam("Año de Egreso") Integer añoEgreso){
        service.guardarEstudiante(nombreEstudiante, apellidoEstudiante, rutEstudiante, fechaNacimiento, tipoColegio,nombreColegio,añoEgreso);
        return "redirect:/nuevo-estudiante";
    }

}
