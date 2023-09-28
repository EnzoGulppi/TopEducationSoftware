package Pep1.services;

import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.EstudiantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EstudiantesService {

    @Autowired
    EstudiantesRepository estudiantesRepository;

    public void guardarEstudiante(
            String nombreEstudiante,
            String apellidoEstudiante,
            String rutEstudiante,
            String fechaNacimiento,
            String tipoColegio,
            String nombreColegio,
            Integer añoEgreso){

        EstudiantesEntity estudiante = new EstudiantesEntity();
        estudiante.setNombreEstudiante(nombreEstudiante);
        estudiante.setApellidoEstudiante(apellidoEstudiante);
        estudiante.setRutEstudiante(rutEstudiante);
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegio(tipoColegio);
        estudiante.setNombreColegio(nombreColegio);
        estudiante.setAñoEgreso(añoEgreso);
        estudiantesRepository.save(estudiante);
    }
    public ArrayList<EstudiantesEntity> obtenerEstudiantes(){
        return (ArrayList<EstudiantesEntity>) estudiantesRepository.findAll();
    }
}
