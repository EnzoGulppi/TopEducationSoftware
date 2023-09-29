package Pep1.services;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.CuotaRepository;
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
}
