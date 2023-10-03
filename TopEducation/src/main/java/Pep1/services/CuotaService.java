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


    //##################################################################################################
    //Mejorar la implementacion de las ideas
    //Calculamos descuento por tipo de procedencia
    public double calcularDescuentoColegioProc(String rutEstudiante){
        //Buscamos por rut al estudiante
        EstudiantesEntity estudiantes = EstudiantesRepository.findByRut(rutEstudiante); //Implementar findByRut en repositorio
        return obtenerDescuentoTipoProc(estudiantes);
    }
    public double obtenerDescuentoTipoProc(EstudiantesEntity estudiantes){
        double matricula = 70000;
        double arancel = 1500000;
        double descuento = 0;

        switch (estudiantes.getTipoColegio().toLowerCase()) {
            case "municipal":
                descuento = 0.20;
                break;
            case "subvencionado":
                descuento = 0.10;
                break;
            case "privado":
                descuento = 0;
                break;
        }

        //if (estudiantes.isEsPagoAlContado()) {
        //    descuento += 0.50;
        //}

        double arancelDescuento = arancel * (1-descuento);
        return matricula + arancelDescuento;
    }

    //##################################################################################################
    public double calcularCuotasColegioProc(EstudiantesEntity estudiantes) {
        double matricula = 70000;
        double arancel = 1500000;
        double descuento = 0;

        int maxCuotas = 0;

        switch (estudiantes.getTipoColegio().toLowerCase()) {
            case "municipal":
                maxCuotas = 10;
                break;
            case "subvencionado":
                maxCuotas = 7;
                break;
            case "privado":
                maxCuotas = 4;
                break;
        }
        //Implementamos una validacion para ver que no exceda
        //if(estudiantes.getNumeroDeCuotas() > maxCuotas) {
        //throw new IllegalArgumentException("Número de cuotas excede el máximo permitido");
        //}
        return 0;
    }




}
