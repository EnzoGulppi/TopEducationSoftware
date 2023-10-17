package Pep1.services;

import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.EstudiantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;


@Service
public class EstudiantesService {

    @Autowired
    private EstudiantesRepository estudiantesRepository;

    public EstudiantesEntity guardarEstudiante(
            String nombreEstudiante,
            String apellidoEstudiante,
            String rutEstudiante,
            Date fechaNacimiento,
            String tipoColegio,
            String nombreColegio,
            Integer egreso){

        EstudiantesEntity estudiantes = new EstudiantesEntity();
        estudiantes.setNombreEstudiante(nombreEstudiante);
        estudiantes.setApellidoEstudiante(apellidoEstudiante);
        estudiantes.setRutEstudiante(rutEstudiante);
        estudiantes.setFechaNacimiento(fechaNacimiento);
        estudiantes.setTipoColegio(tipoColegio);
        estudiantes.setNombreColegio(nombreColegio);
        estudiantes.setEgreso(egreso);
        estudiantesRepository.save(estudiantes);
        return estudiantes;
    }
    public ArrayList<EstudiantesEntity> obtenerEstudiantes(){
        return (ArrayList<EstudiantesEntity>)  estudiantesRepository.findAll();
    }


    public EstudiantesEntity guardarEstudiantes(EstudiantesEntity estudiantes){
        return estudiantesRepository.save(estudiantes);
    }
}
