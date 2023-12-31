package Pep1.controllers;

import Pep1.entities.EstudiantesEntity;
import Pep1.services.EstudiantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;


@Controller
@RequestMapping
public class EstudiantesController {

    @Autowired
    private EstudiantesService estudiantesService;

    @GetMapping("/lista-estudiante")
    public String listar(Model model) {
        ArrayList<EstudiantesEntity> estudiantes =  estudiantesService.obtenerEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "index";
    }

    @GetMapping("/nuevo-estudiante")
    public String estudiante(){
        return "nuevo-estudiante";
    }
    @PostMapping("/nuevo-estudiante")
    public String nuevoEstudiante(@RequestParam("nombreEstudiante") String nombreEstudiante,
                                  @RequestParam("apellidoEstudiante") String apellidoEstudiante,
                                  @RequestParam("rutEstudiante") String rutEstudiante,
                                  @RequestParam("fechaNacimiento") Date fechaNacimiento,
                                  @RequestParam("tipoColegio") String tipoColegio,
                                  @RequestParam("nombreColegio") String nombreColegio,
                                  @RequestParam("egreso") Integer egreso
                                  ){
        estudiantesService.guardarEstudiante(nombreEstudiante, apellidoEstudiante, rutEstudiante, fechaNacimiento, tipoColegio,nombreColegio,egreso);
        return "redirect:/nuevo-estudiante";
    }

}



