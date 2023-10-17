package Pep1;

import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.EstudiantesRepository;
import Pep1.services.EstudiantesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.Assert.*;

@SpringBootTest
public class EstudiantesTest {
    @Autowired
    private EstudiantesService estudiantesService;

    @Autowired
    private EstudiantesRepository estudiantesRepository;


    @Test
    public void testGuardarEstudiante() {
        // Crear un estudiante simulado
        EstudiantesEntity estudiante = new EstudiantesEntity();
        estudiante.setNombreEstudiante("Nombre");
        estudiante.setApellidoEstudiante("Apellido");
        estudiante.setRutEstudiante("123456789");
        estudiante.setFechaNacimiento(new Date(System.currentTimeMillis()));
        estudiante.setTipoColegio("Colegio");
        estudiante.setNombreColegio("NombreColegio");
        estudiante.setEgreso(2023);

        // Llamar al método que queremos probar
        EstudiantesEntity resultado = estudiantesService.guardarEstudiante(
                "Nombre",
                "Apellido",
                "123456789",
                new Date(System.currentTimeMillis()),
                "Colegio",
                "NombreColegio",
                2023
        );

        // Verificar que el resultado no sea nulo
        assertNotNull(resultado);
        // Verificar que el estudiante se haya guardado correctamente en la base de datos
        EstudiantesEntity estudianteGuardado = estudiantesRepository.findByRutEstudiante(resultado.getRutEstudiante());
        assertNotNull(estudianteGuardado);
        assertEquals(estudiante, estudianteGuardado);
    }
    @Test
    public void testObtenerEstudiantes() {
        // Crear una lista simulada de estudiantes
        EstudiantesEntity estudiante1 = new EstudiantesEntity();
        estudiante1.setNombreEstudiante("Estudiante1");
        estudiantesRepository.save(estudiante1);

        EstudiantesEntity estudiante2 = new EstudiantesEntity();
        estudiante2.setNombreEstudiante("Estudiante2");
        estudiantesRepository.save(estudiante2);

        // Llamar al método que queremos probar
        ArrayList<EstudiantesEntity> resultado = estudiantesService.obtenerEstudiantes();

        // Verificar que el resultado no sea nulo y tenga el mismo tamaño que la lista simulada
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        // Verificar que los elementos de la lista simulada estén presentes en el resultado
        assertTrue(resultado.contains(estudiante1));
        assertTrue(resultado.contains(estudiante2));
    }
}
