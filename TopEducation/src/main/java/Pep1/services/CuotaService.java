package Pep1.services;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.CuotaRepository;
import Pep1.repositories.EstudiantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class CuotaService {
    @Autowired
    private CuotaRepository cuotaRepository;

    //Metodo para generar cuotas
    public ArrayList<CuotaEntity> generarCuotas(EstudiantesEntity estudiantes, BigDecimal montoTotal, int numCuotas, LocalDate fechaInicio){
        BigDecimal montoCuota = montoTotal.divide(BigDecimal.valueOf(numCuotas), RoundingMode.HALF_UP);
        ArrayList<CuotaEntity> cuotas = new ArrayList<>();
        for (int i = 0; i< numCuotas; i++){
            CuotaEntity cuota = new CuotaEntity();
            cuota.setMonto(montoCuota);
            cuota.setVencimiento(fechaInicio.plusMonths(i));
            cuota.setEstudiante(estudiantes);
            cuotas.add(cuota);
        }
        return (ArrayList<CuotaEntity>) cuotaRepository.saveAll(cuotas);
    }





























    //Dejamos la matricula fija
    public boolean esMatriculaPagada(String rutEstudiante) {
        List<CuotaEntity> cuotasDeMatricula = cuotaRepository.findByEstudianteRutEstudianteAndEsMatricula(rutEstudiante, true);
        //Declaramos el monto de la matricula
        BigDecimal montoMatriculaEsperado = new BigDecimal("70000");
        //Verificamos si el estudiante pago matricula
        for (CuotaEntity cuota : cuotasDeMatricula) {
            if ("Pagado".equals(cuota.getEstado()) && montoMatriculaEsperado.compareTo(cuota.getMonto()) == 0) {
                return true;
            }
        }
        return false;
    }
    //Generamos cuotas para el estudiante
    public Integer calcularCuotasTipoColegio(String rut){
        EstudiantesEntity estudiantes = EstudiantesRepository.findByTipoColegio(rut);
        return obtenerCuotasTipoColegio(estudiantes);
    }

    //REVISAAAAAAAAAAR
    public Integer obtenerCuotasTipoColegio(EstudiantesEntity estudiantes){
        Integer maxCuotas = 0;
        double descuento = 0;
        if(estudiantes.getTipoColegio().equals("Municipal")){
            descuento = 0.2;
            maxCuotas=10;
        } else if (estudiantes.getTipoColegio().equals("Subencionado")) {
            descuento = 0.10;
            maxCuotas=7;
        } else if (estudiantes.getTipoColegio().equals("Privado")) {
            maxCuotas=4;
        }
        return null;
    }



    //PRIMERO HAY QUE VER SI EXISTEN CUOTAS




    //##################################################################################################
    //Mejorar la implementacion de las ideas
    //Calculamos descuento por tipo de procedencia


    //##################################################################################################




}
