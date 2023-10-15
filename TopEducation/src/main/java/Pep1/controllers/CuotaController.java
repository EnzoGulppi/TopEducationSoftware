package Pep1.controllers;

import Pep1.entities.CuotaEntity;
import Pep1.services.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.util.ArrayList;

@Controller
@RequestMapping
public class CuotaController {
    @Autowired
    private CuotaService cuotaService;
    @GetMapping("/resumen-cuotas")
    public String cuotas(){
        return "resumen-cuotas";
    }

    @GetMapping("/Pedir-rut-estudiante")
    public String PedirRutParaCuotas() {
        return "cuotasPorRut";
    }
    @GetMapping("/BuscarCuotasPorRut/{Rut}")
    public String listaCuotasPorRutEstudiante(@RequestParam("rutEstudiante") String rut, Model model) {
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRutEstudiante(rut);
        model.addAttribute("cuotas", cuotas);
        model.addAttribute("rutIngresado", rut);
        return "resumen-cuotas";
    }
    @GetMapping("/DetalleCuota/{idCuota}")
    public String detalleCuota(@PathVariable("idCuota") Long idCuota, Model model) {
        CuotaEntity cuota = cuotaService.BuscarPorID(idCuota);
        model.addAttribute("cuota", cuota);
        return "detalleCuota";
    }

    @GetMapping("/RegistrarPagoCuota/{idCuota}")
    public String RegistrarPagoCuota(@PathVariable("idCuota") Long idCuota, Model model) {
        /*Búsqueda de Cuota especificada*/
        CuotaEntity cuota = cuotaService.registrarEstadoDePagoCuota(idCuota);
        //Agrega la cuota al modelo para que se pueda mostrar en la vista
        model.addAttribute("cuota", cuota);
        return "detalleCuota";
    }
    @GetMapping("/generar-cuotas")
    public String GenerarCuotasFormulario() {

        return "registrar-pago";
    }
    @PostMapping("/registrar-cuotas")
    public String generarCuotas(@RequestParam("rutEstudiante") String rut,
                                @RequestParam("numeroCuotas") Integer cantCuotas,
                                @RequestParam("tipoPago") String tipoPago,
                                Model model) {
        ArrayList<CuotaEntity> error = cuotaService.generarCuotasEstudiante(rut, cantCuotas, tipoPago);

        int errores = error.get(0).getAtraso();

        if (errores == -1) {
            model.addAttribute("mensaje", "Pago al contado es único");
        } else if (errores == -2) {
            model.addAttribute("mensaje", "Ya hay cuotas asociadas al rut");
        } else if (errores == -3) {
            model.addAttribute("mensaje", "Un alumno de un colegio municipal solo opta a máximo 10 cuotas");
        } else if (errores == -4) {
            model.addAttribute("mensaje", "Un alumno de un colegio subvencionado solo opta a máximo 7 cuotas");
        } else if (errores == -5) {
            model.addAttribute("mensaje", "Un alumno de un colegio privado solo opta a máximo 4 cuotas");
        } else if (errores == -6) {
            model.addAttribute("mensaje", "Rut dado no está registrado");
        } else {
            model.addAttribute("mensaje", "Cuotas generadas satisfactoriamente.");
        }

        return "redirect:/resumen-cuotas";
    }






















    /*@GetMapping("/resumen-cuotas")
    public String listarCuotas(Model model) {
        ArrayList<CuotaEntity> cuotas =  cuotaService.listaArancel();
        model.addAttribute("cuotas", cuotas);
        return "resumen-cuotas";
    }

    @GetMapping("/registrar-pago")
    public String cuotas(){
        return "registrar-pago";
    }
    @PostMapping("/registrar-pago")
    public String registrarPagos(@RequestParam("numeroCuotas") Integer numeroCuotas)
    {
        //String rut = (String) session.getAttribute("rutEstudiante");
        cuotaService.guardarNumCuotas(numeroCuotas);
        return "redirect:/resumen-cuotas";
    }

    @GetMapping("/resumen-cuotas/crear")
    public String crearPlanillaArancel(Model model) {
        boolean planillasGeneradas = cuotaService.calcularArancel();
        if (planillasGeneradas) {
            model.addAttribute("planillasGeneradas", true);
        }
        return "resumen-cuotas";
    }*/
    /*@GetMapping("/resumen-cuotas")
    public String mostrarArancelEstudiante(Model model) {
        List<CuotaEntity> listaPagosArancel = cuotaService.listaArancel();
        model.addAttribute("cuotas", listaPagosArancel);
        return "resumen-cuotas";
    }

    @GetMapping("/registrar-pago")
    public String actualizarCuotaEstudiante(Model model){
        boolean planillaActualizada = cuotaService.actualizarCuotaEstudiante();
        if(planillaActualizada){
            model.addAttribute("planillaActualizada", true);
        }
        return "registrar-pago";
    }*/




}
