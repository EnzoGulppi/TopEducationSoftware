package Pep1.controllers;

import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.EstudiantesRepository;
import Pep1.services.EstudiantesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


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
                                  @RequestParam("egreso") Integer egreso,
                                  HttpSession session){ //Mantener la sesion activa para compartir parametros
        session.setAttribute("rutEstudiante",rutEstudiante);
        estudiantesService.guardarEstudiante(nombreEstudiante, apellidoEstudiante, rutEstudiante, fechaNacimiento, tipoColegio,nombreColegio,egreso);
        return "redirect:/nuevo-estudiante";
    }

}



