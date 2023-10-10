package Pep1.services;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.CuotaRepository;
import Pep1.repositories.EstudiantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.List;


@Service
public class CuotaService {
    @Autowired
    private CuotaRepository cuotaRepository;
    @Autowired
    private EstudiantesRepository estudiantesRepository;
    private Double arancel = 1500000.0;

    public void guardarArancel(CuotaEntity cuota){cuotaRepository.save(cuota);}
    public List<CuotaEntity> listaArancel(){
        return cuotaRepository.findAll();
    }

    /*public CuotaEntity buscarEstudiante(String rutEstudiante){
        return estudiantesRepository.findByRutEstudiante(rutEstudiante);
    }*/

    public CuotaEntity buscarPlanillaEstudiante(EstudiantesEntity estudiantes) {
        CuotaEntity planilla = cuotaRepository.findPlanillaByEstudiante(estudiantes);
        return planilla;
    }

    public CuotaEntity crearPlanillaEstudiante(EstudiantesEntity estudiantes) {
        CuotaEntity pago = new CuotaEntity();
        pago.setEstudiante(estudiantes);
        int numCuotas = cuotasDeProcedencia(estudiantes);
        int descuentoEgreso = descuentoPorEgreso(estudiantes);
        double descuentoProcedencia = descuentoDeProcedencia(estudiantes);
        double pagoTotal = arancel - (descuentoEgreso + descuentoProcedencia);
        double pagoPorCuota = pagoTotal / numCuotas;
        pago.setArancel( pagoTotal);
        pago.setNumeroCoutas(numCuotas);
        pago.setPorPagar(pagoPorCuota);
        return pago;
    }

    public boolean calcularArancel() {
        List<EstudiantesEntity> listaEstudiante = estudiantesRepository.findAll();
        boolean lecturaEstudiante = false;
        for (EstudiantesEntity estudiantes : listaEstudiante) {
            if (estudiantes != null) {
                CuotaEntity pagoArancel = crearPlanillaEstudiante(estudiantes);
                guardarArancel(pagoArancel);
                lecturaEstudiante = true;
            }
        }
        return lecturaEstudiante;
    }
    public double descuentoDeProcedencia(EstudiantesEntity estudiantes) {
        String opcionPago = estudiantes.getTipoColegio();
        double descuentoTotal = 0.0;
        if("Municipal".equals(opcionPago)) {
            descuentoTotal = 0.2;
        }else if("Subvencionado".equals(opcionPago)) {
            descuentoTotal = 0.1;
        }else if ("Privado".equals(opcionPago)) {
            descuentoTotal = 0;
        }
        return descuentoTotal * arancel;
    }
    public int cuotasDeProcedencia(EstudiantesEntity estudiantes){
        int maxCuotas;
        if("Municipal".equals(estudiantes.getTipoColegio())){
            maxCuotas = 10;
        } else if ("Subvencionado".equals(estudiantes.getTipoColegio())){
            maxCuotas = 7;
        } else if ("Privado".equals(estudiantes.getTipoColegio())){
            maxCuotas = 4;
        }else{
            maxCuotas = 0;
        }
        return maxCuotas;
    }

    public int descuentoPorEgreso(EstudiantesEntity estudiantes) {
        int descuentoTotal = 0;
        Integer egreso = estudiantes.getEgreso();
        int anioActual = LocalDate.now().getYear();
        int diferenciaAnios = anioActual - egreso;
        if (diferenciaAnios < 1) {
            descuentoTotal = 15;
        } else if (diferenciaAnios >= 1 && diferenciaAnios <= 2) {
            descuentoTotal = 8;
        } else if (diferenciaAnios >= 3 && diferenciaAnios <= 4) {
            descuentoTotal = 4;
        } else {
            descuentoTotal = 0;
        }

        int descuento = (int) ((descuentoTotal * arancel) / 100);
        return descuento;
    }

}

















