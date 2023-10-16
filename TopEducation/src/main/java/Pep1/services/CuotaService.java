package Pep1.services;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.CuotaRepository;
import Pep1.repositories.EstudiantesRepository;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        EstudiantesEntity estudiantes = estudiantesRepository.findByRutEstudiante(rut);
        ArrayList<CuotaEntity> cuotasGeneradas = new ArrayList<>();

        if (estudiantes == null || tieneCuotasPrevias(estudiantes) || tieneErroresDeEntrada(estudiantes, cantidad, tipo)) {
            cuotasGeneradas.add(crearErrorCuota(estudiantes, cantidad, tipo));
            return cuotasGeneradas;
        }

        cuotasGeneradas.add(crearCuotaDeMatricula(estudiantes));
        float arancelConDescuentos = calcularArancelConDescuentos(estudiantes);

        if ("Contado".equals(tipo)) {
            cuotasGeneradas.add(crearCuotaContado(estudiantes, arancelConDescuentos / 2));
        } else {
            cuotasGeneradas.addAll(crearCuotas(estudiantes, cantidad, arancelConDescuentos / cantidad));
        }

        return cuotasGeneradas;
    }

    public boolean tieneCuotasPrevias(EstudiantesEntity estudiantes) {
        return !cuotaRepository.findAllByEstudianteId(estudiantes.getIdEstudiante()).isEmpty();
    }

    public boolean tieneErroresDeEntrada(EstudiantesEntity estudiantes, Integer cantidad, String tipo) {
        //Más validaciones según sean necesarias
        return cantidad > 1 && "Contado".equals(tipo);
    }

    public CuotaEntity crearErrorCuota(EstudiantesEntity estudiantes, Integer cantidad, String tipo) {
        CuotaEntity errorCuota = new CuotaEntity();
        errorCuota.setAtraso(-1);
        return errorCuota;
    }

    public CuotaEntity crearCuotaDeMatricula(EstudiantesEntity estudiante) {
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

    public float calcularArancelConDescuentos(EstudiantesEntity estudiantes) {
        double descuento = 0;

        if ("Municipal".equals(estudiantes.getTipoColegio())) {
            descuento = (double) 1500000 * 0.20;
        } else if ("Subvencionado".equals(estudiantes.getTipoColegio())) {
            descuento = (double) 1500000 * 0.10;
        }

        if (estudiantes.getEgreso() == 0) {
            descuento += (double) 1500000 * 0.15;
        } else if (estudiantes.getEgreso() <= 2) {
            descuento += (double) 1500000 * 0.08;
        } else if (estudiantes.getEgreso() <= 4) {
            descuento += (double) 1500000 * 0.04;
        }
        double total = (double) 1500000 - descuento;
        return (float) total;
    }

    public CuotaEntity crearCuotaContado(EstudiantesEntity estudiante, double monto) {
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

    public List<CuotaEntity> crearCuotas(EstudiantesEntity estudiante, Integer cantidad, double monto) {
        List<CuotaEntity> cuotas = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            CuotaEntity cuota = new CuotaEntity();
            cuota.setIdEstudiante(estudiante.getIdEstudiante());
            cuota.setArancel(monto / cantidad);
            cuota.setTipoPago("Cuotas");
            cuota.setEstadoCuota("Pendiente");
            cuota.setMontoTotalPagado(monto / cantidad);
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



