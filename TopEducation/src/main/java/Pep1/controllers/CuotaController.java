package Pep1.controllers;

import Pep1.entities.CuotaEntity;
import Pep1.services.CuotaService;
import Pep1.services.EstudiantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping
public class CuotaController {
    @Autowired
    private CuotaService cuotaService;
    @GetMapping("/resumen-cuotas")
    public String cuotas(){
        return "resumen-cuotas";
    }
    @GetMapping("/formulario-cuotas")
    public String crearPlanillaArancel(Model model) {
        boolean planillasGeneradas = cuotaService.calcularArancel();
        if (planillasGeneradas) {
            model.addAttribute("planillasGeneradas", true);
        }
        return "formulario-cuotas";
    }
    @GetMapping("/resumen-cuotas/detalle")
    public String mostrarArancelEstudiante(Model model) {
        List<CuotaEntity> listaPagosArancel = cuotaService.listaArancel();
        model.addAttribute("cuota", listaPagosArancel);
        return "resumen-cuotas";
    }


}
