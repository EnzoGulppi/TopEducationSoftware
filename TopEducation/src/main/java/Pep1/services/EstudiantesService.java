package Pep1.services;

import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.EstudiantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;


@Service
public class EstudiantesService {

    @Autowired
    private EstudiantesRepository estudiantesRepository;

    public void guardarEstudiante(
            String nombreEstudiante,
            String apellidoEstudiante,
            String rutEstudiante,
            LocalDate fechaNacimiento,
            String tipoColegio,
            String nombreColegio,
            Integer egreso){

        EstudiantesEntity estudiante = new EstudiantesEntity();
        estudiante.setNombreEstudiante(nombreEstudiante);
        estudiante.setApellidoEstudiante(apellidoEstudiante);
        estudiante.setRutEstudiante(rutEstudiante);
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegio(tipoColegio);
        estudiante.setNombreColegio(nombreColegio);
        estudiante.setEgreso(egreso);
        estudiantesRepository.save(estudiante);
    }
    public ArrayList<EstudiantesEntity> obtenerEstudiantes(){
        return (ArrayList<EstudiantesEntity>)  estudiantesRepository.findAll();
    }
    public EstudiantesEntity findByRutEstudiante(String rutEstudiante){
        return estudiantesRepository.findByRutEstudiante(rutEstudiante);
    }
}
