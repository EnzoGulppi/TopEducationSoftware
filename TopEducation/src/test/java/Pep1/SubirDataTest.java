package Pep1;

import Pep1.entities.EstudiantesEntity;
import Pep1.entities.SubirDataEntity;
import Pep1.repositories.EstudiantesRepository;
import Pep1.services.EstudiantesService;
import Pep1.services.SubirDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SubirDataTest {
    @Autowired
    private EstudiantesRepository estudiantesRepository;

    @Autowired
    private EstudiantesService estudiantesService;

    @Autowired
    private SubirDataService pruebaService;

    @Test
    public void ObtenerPruebasEstudianteRutExiste() {
        /*Elementos Internos*/
        EstudiantesEntity estudiante;   //Estudiante de prueba.
        ArrayList<SubirDataEntity> Pruebas;    //Pruebas

        /*Modelo de estudiante usual que se guarda en la BD*/
        estudiante = new EstudiantesEntity(); //Creación.
        //Id de entidad actualizable de forma automatica
        estudiante.setRutEstudiante("20.453.333-k");
        estudiante.setApellidoEstudiante("Ramirez Baeza");
        estudiante.setNombreEstudiante("Elvio Camba");
        estudiante.setTipoColegio("Privado");
        estudiante.setNombreColegio("Weston Academy");
        estudiante.setEgreso(2002);

        /*Guardado en la base de datos*/
        estudiantesService.guardarEstudiantes(estudiante);
        Pruebas = pruebaService.ObtenerPruebasPorRutEstudiante(estudiante.getRutEstudiante());
        estudiantesRepository.delete(estudiante);

        /*Verificar resultado*/
        assertEquals(Pruebas.size(),0,0);
    }

    @Test
    public void ObtenerPruebasEstudianteRutNoExiste() {
        /*Elementos Internos*/
        ArrayList<SubirDataEntity> Pruebas;    //Pruebas

        Pruebas = pruebaService.ObtenerPruebasPorRutEstudiante("..........");

        assertEquals(Pruebas.size(),1,0);
    }

    @Test
    public void PromediosPruebasEstudiante() {
        /*Elementos internos*/
        ArrayList<SubirDataEntity> Pruebas = new ArrayList<>();    //Pruebas
        SubirDataEntity ElementoAuxiliar = new SubirDataEntity();
        Integer PromedioListaVacia;
        Integer PromedioLista1Elemento;

        //Se calcula promedio con lista vacia.
        PromedioListaVacia = pruebaService.PromediosPruebasEstudiante(Pruebas);

        //Se genera añade elemto dummy.
        ElementoAuxiliar.setPuntaje(100);
        Pruebas.add(ElementoAuxiliar);

        //Se calcula promedio con 1 elemento.
        PromedioLista1Elemento = pruebaService.PromediosPruebasEstudiante(Pruebas);

        /*Validar resultados*/
        assertEquals(PromedioListaVacia,0,0);
        assertEquals(PromedioLista1Elemento,100,0);
    }
    

}
