package Pep1.services;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.CuotaRepository;
import Pep1.repositories.EstudiantesRepository;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class CuotaService {
    @Autowired
    private CuotaRepository cuotaRepository;
    @Autowired
    private EstudiantesRepository estudiantesRepository;


    public CuotaEntity BuscarPorID(Long idCuota){ return cuotaRepository.findByIdCuota(idCuota);}

    public void guardarArancel(CuotaEntity cuota) {
        cuotaRepository.save(cuota);
    }
    /*public void guardarNumCuotas(Integer numeroCoutas) {
        CuotaEntity cuota = new CuotaEntity();
        cuota.setNumeroCuotas(numeroCoutas);
        cuotaRepository.save(cuota);
    }*/

    public ArrayList<CuotaEntity> obtenerCuotasPorRutEstudiante(String rut) {
        EstudiantesEntity estudiantes = estudiantesRepository.findByRutEstudiante(rut);
        if (estudiantes == null) {
            CuotaEntity cuotaEntity = new CuotaEntity();
            cuotaEntity.setAtraso(-1);
            //Creamos una lista inmutable y la pasamos a un array
            return new ArrayList<>(Collections.singletonList(cuotaEntity));
        }
        return cuotaRepository.findAllByEstudianteId(estudiantes.getIdEstudiante());
    }
    public CuotaEntity registrarEstadoDePagoCuota(Long idCuota) {
        CuotaEntity cuotaExistente = cuotaRepository.findByIdCuota(idCuota);

        if (!"Pagado".equals(cuotaExistente.getEstadoCuota())) {
            //Ocupamos operador ternario
            cuotaExistente.setEstadoCuota("Atrasada".equals(cuotaExistente.getEstadoCuota()) ? "Pagado (con atraso)" : "Pagado");
            cuotaExistente.setCreacionCuota(cuotaExistente.getPagoCuota());
            cuotaExistente.setPagoCuota(LocalDate.now());
            cuotaRepository.save(cuotaExistente);
        }
        return cuotaExistente;
    }

    public ArrayList<CuotaEntity> generarCuotasEstudiante(String rut, Integer cantidad, String tipo) {
        EstudiantesEntity estudiante = estudiantesRepository.findByRutEstudiante(rut);
        ArrayList<CuotaEntity> cuotasGeneradas = new ArrayList<>();

        if (estudiante == null || tieneCuotasPrevias(estudiante) || tieneErroresDeEntrada(estudiante, cantidad, tipo)) {
            cuotasGeneradas.add(crearErrorCuota(estudiante, cantidad, tipo));
            return cuotasGeneradas;
        }

        cuotasGeneradas.add(crearCuotaDeMatricula(estudiante));
        float arancelConDescuentos = calcularArancelConDescuentos(estudiante, 1500000);

        if ("Contado".equals(tipo)) {
            cuotasGeneradas.add(crearCuotaContado(estudiante, arancelConDescuentos / 2));
        } else {
            cuotasGeneradas.addAll(crearCuotas(estudiante, cantidad, arancelConDescuentos / cantidad));
        }

        return cuotasGeneradas;
    }

    private boolean tieneCuotasPrevias(EstudiantesEntity estudiante) {
        return !cuotaRepository.findAllByEstudianteId(estudiante.getIdEstudiante()).isEmpty();
    }

    private boolean tieneErroresDeEntrada(EstudiantesEntity estudiante, Integer cantidad, String tipo) {
        //Más validaciones según sean necesarias
        return cantidad > 1 && "Contado".equals(tipo);
    }

    private CuotaEntity crearErrorCuota(EstudiantesEntity estudiante, Integer cantidad, String tipo) {
        CuotaEntity errorCuota = new CuotaEntity();
        errorCuota.setAtraso(-1);
        return errorCuota;
    }

    private CuotaEntity crearCuotaDeMatricula(EstudiantesEntity estudiante) {
        CuotaEntity matricula = new CuotaEntity();
        matricula.setIdEstudiante(estudiante.getIdEstudiante());
        matricula.setArancel(70000.0);
        matricula.setTipoPago("Contado");
        matricula.setEstadoCuota("Pagado");
        matricula.setMontoTotalPagado(70000.0);
        matricula.setCreacionCuota(LocalDate.now());
        matricula.setPagoCuota(LocalDate.now());
        matricula.setAtraso(0);
        cuotaRepository.save(matricula);
        return matricula;
    }

    private float calcularArancelConDescuentos(EstudiantesEntity estudiante, float arancelBase) {
        float descuento = 0;

        if ("Municipal".equals(estudiante.getTipoColegio())) {
            descuento = arancelBase * 0.20f;
        } else if ("Subvencionado".equals(estudiante.getTipoColegio())) {
            descuento = arancelBase * 0.10f;
        }

        if (estudiante.getEgreso() == 0) {
            descuento += arancelBase * 0.15f;
        } else if (estudiante.getEgreso() <= 2) {
            descuento += arancelBase * 0.08f;
        } else if (estudiante.getEgreso() <= 4) {
            descuento += arancelBase * 0.04f;
        }

        return arancelBase - descuento;
    }

    private CuotaEntity crearCuotaContado(EstudiantesEntity estudiante, double monto) {
        CuotaEntity cuota = new CuotaEntity();
        cuota.setIdEstudiante(estudiante.getIdEstudiante());
        cuota.setArancel(monto);
        cuota.setTipoPago("Contado");
        cuota.setEstadoCuota("Pendiente");
        cuota.setMontoTotalPagado(monto);
        cuota.setCreacionCuota(LocalDate.now());
        cuota.setAtraso(0);
        cuotaRepository.save(cuota);
        return cuota;
    }

    private List<CuotaEntity> crearCuotas(EstudiantesEntity estudiante, Integer cantidad, double monto) {
        List<CuotaEntity> cuotas = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            CuotaEntity cuota = new CuotaEntity();
            cuota.setIdEstudiante(estudiante.getIdEstudiante());
            cuota.setArancel(monto);
            cuota.setTipoPago("Cuotas");
            cuota.setEstadoCuota("Pendiente");
            cuota.setMontoTotalPagado(monto);
            cuota.setCreacionCuota(LocalDate.now());
            cuota.setAtraso(0);
            cuotaRepository.save(cuota);
            cuotas.add(cuota);
        }
        return cuotas;
    }

    public Integer ContarCuotasPagadas(ArrayList<CuotaEntity> Cuotas){
        Integer CanCuotasPagadas = 0;
        int i = 0;

        while (i < Cuotas.size()){
            if(Cuotas.get(i).getEstadoCuota().equals("Pagado")){
                CanCuotasPagadas++;
            }
            i++;
        }
        return CanCuotasPagadas;
    }
    public Integer ContarCuotasAtrasadas(ArrayList<CuotaEntity> Cuotas){
        Integer CanCuotasAtrasadas = 0;
        int i = 0;

        while (i < Cuotas.size()){
            if(Cuotas.get(i).getAtraso() != 0){
                CanCuotasAtrasadas++;
            }
            i++;
        }

        return CanCuotasAtrasadas;
    }
    public String FechaUltimaCuotaPagada(ArrayList<CuotaEntity> Cuotas){

        String Fecha = "";
        int i = Cuotas.size()-1;
        while (i < 0){
            if(Cuotas.get(i).getEstadoCuota().equals("Pagado")){
                Fecha = Cuotas.get(i).getPagoCuota().toString();
            }
            i--;
        }
        return Fecha;
    }
    @Generated
    public ArrayList<CuotaEntity> ActualizarCuotas(ArrayList<CuotaEntity> Cuotas){
        return (ArrayList<CuotaEntity>) cuotaRepository.saveAll(Cuotas);
    }


}



