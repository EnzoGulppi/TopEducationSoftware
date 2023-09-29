package Pep1.controllers;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.services.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.model.IModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/cuotas")
public class CuotaController {
    @Autowired
    private CuotaService cuotaService;

    @PostMapping("/generar")
    public String generarCuotas(@RequestParam("rutEstudiante") String rutEstudiante,
                                @RequestParam("montoTotal") BigDecimal montoTotal,
                                @RequestParam("numCuotas") int numCuotas,
                                @RequestParam("fechaInicio") LocalDate fechaInicio,
                                Model model){
        //Cargamos el estudiante por ID-> En este caso por su rut
        EstudiantesEntity estudiantes = new EstudiantesEntity();
        estudiantes.setRutEstudiante(rutEstudiante);

        //Enlistamos las cuotas
        ArrayList<CuotaEntity> cuotas = cuotaService.generarCuotas(estudiantes, montoTotal, numCuotas, fechaInicio);
        model.addAttribute("cuotas", cuotas);
        return "resumen-cuotas";
    }
}
