package com.TopEducation.TopEducation;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.CuotaRepository;
import Pep1.repositories.EstudiantesRepository;
import Pep1.services.CuotaService;
import Pep1.services.EstudiantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;




import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CuotaTest {
    @Autowired
    private CuotaService cuotaService;

    @Autowired
    private EstudiantesService estudiantesService;

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private EstudiantesRepository estudiantesRepository;


    @Test
    public void ListarCuotas_RutNoExiste() {
        ArrayList<CuotaEntity> cuotas;
        //Se coloca rut ficticio que no existe debido a formato
        cuotas = cuotaService.obtenerCuotasPorRutEstudiante("hjsdfbsj");

        /*Se establece modelo de cuota cuando usuario no existe*/
        assertNull(cuotas.get(0).getIdCuota());
        assertNull(cuotas.get(0).getIdEstudiante());
        assertNull(cuotas.get(0).getArancel());
        assertNull(cuotas.get(0).getTipoPago());
        assertNull(cuotas.get(0).getEstadoCuota());
        assertNull(cuotas.get(0).getMontoTotalPagado());
        assertNull(cuotas.get(0).getCreacionCuota());
        assertNull(cuotas.get(0).getPagoCuota());
        assertEquals(cuotas.get(0).getAtraso(),-1,0);
    }
    @Test
    public void ListarCuotas_CuotasNoExisten() {
        //Elementos Internos.
        ArrayList<CuotaEntity> cuotas;
        EstudiantesEntity estudiantes = new EstudiantesEntity();   //Estudiante de prueba.

        //Se genera estudiante de prueba Dummy (esto para evitar errores)
        estudiantes.setRutEstudiante("prueba1");
        estudiantes.setApellidoEstudiante("Ramirez Baeza");
        estudiantes.setNombreEstudiante("Elvio Camba");
        estudiantes.setTipoColegio("Privado");
        estudiantes.setNombreColegio("Weston Academy");
        estudiantes.setEgreso(4);

        //Se guarda estudiante
        estudiantes = estudiantesService.guardarEstudiantes(estudiantes);

        //Se establece un rut existente para la busqueda de cuotas
        cuotas = cuotaService.obtenerCuotasPorRutEstudiante("prueba1");
        estudiantesRepository.delete(estudiantes);

        //Se verifica lista vacia
        assertTrue(cuotas.isEmpty());
    }
    @Test
    public void testGenerarCuotas_MinimoExcedido() {
        EstudiantesEntity estudiante1 = crearEstudianteDePrueba("prueba1", "Lopez Perez", "Ana Maria", "Privado", "Colegio A", 2018);
        EstudiantesEntity estudiante2 = crearEstudianteDePrueba("prueba2", "Ramirez Baeza", "Elvio Camba", "Subvencionado", "Colegio B", 2017);
        EstudiantesEntity estudiante3 = crearEstudianteDePrueba("prueba3", "Gonzalez Rodriguez", "Pedro", "Municipal", "Colegio C", 2019);

        ArrayList<CuotaEntity> cuotas1 = generarYEliminarCuotas(estudiante1, "Cuotas");
        ArrayList<CuotaEntity> cuotas2 = generarYEliminarCuotas(estudiante2, "Cuotas");
        ArrayList<CuotaEntity> cuotas3 = generarYEliminarCuotas(estudiante3, "Cuotas");

        assertEquals(cuotas1.get(0).getAtraso(), -3, 0);
        assertEquals(cuotas2.get(0).getAtraso(), -4, 0);
        assertEquals(cuotas3.get(0).getAtraso(), -2, 0);
    }

    public EstudiantesEntity crearEstudianteDePrueba(String rut, String apellidos, String nombres, String tipoColegio, String nombreColegio, int egreso) {
        EstudiantesEntity estudiante = new EstudiantesEntity();
        estudiante.setRutEstudiante(rut);
        estudiante.setApellidoEstudiante(apellidos);
        estudiante.setNombreEstudiante(nombres);
        estudiante.setFechaNacimiento(new Date());
        estudiante.setTipoColegio(tipoColegio);
        estudiante.setNombreColegio(nombreColegio);
        estudiante.setEgreso(egreso);
        return estudiantesService.guardarEstudiantes(estudiante);
    }

    public ArrayList<CuotaEntity> generarYEliminarCuotas(EstudiantesEntity estudiante, String tipoCuotas) {
        estudiante = estudiantesService.guardarEstudiantes(estudiante);
        ArrayList<CuotaEntity> cuotas = cuotaService.generarCuotasEstudiante(estudiante.getRutEstudiante(), 100, tipoCuotas);
        cuotaRepository.deleteAll(cuotas);
        estudiantesRepository.delete(estudiante);
        return cuotas;
    }

    @Test
    public void GenerarCuotas_RutInexistente() {
        ArrayList<CuotaEntity> cuotas;
        //Generar cuotas por caso
        cuotas = cuotaService.generarCuotasEstudiante(".........",100,"Contado");
        cuotaRepository.deleteAll(cuotas);
        //Verificar resultados
        assertEquals(cuotas.get(0).getAtraso(),-6,0);
    }
    @Test
    public void BuscarCuotaPorID(){
        //Elementos Internos.
        EstudiantesEntity estudiante = new EstudiantesEntity();   //Estudiante de prueba.
        ArrayList<CuotaEntity> cuotas;  //Cuotas generadas.
        CuotaEntity Test;       //Cuota de prueba

        /*Se genera estudiante de prueba Dummy (esto para evitar errores)*/
        estudiante.setRutEstudiante("prueba2");
        estudiante.setApellidoEstudiante("Ramirez Baeza");
        estudiante.setNombreEstudiante("Elvio Camba");
        estudiante.setFechaNacimiento(new Date());
        estudiante.setTipoColegio("Privado");
        estudiante.setNombreColegio("Weston Academy");
        estudiante.setEgreso(4);

        /*Generar cuotas y buscar por ID*/
        estudiante = estudiantesService.guardarEstudiantes(estudiante);
        cuotas = cuotaService.generarCuotasEstudiante("prueba2",1,"Contado");
        Test = cuotaService.BuscarPorID(cuotas.get(0).getIdCuota());
        cuotaRepository.deleteAll(cuotas);
        estudiantesRepository.delete(estudiante);

        /*Comprobar resultados*/
        assertEquals(cuotas.get(0).getArancel(),(float) 70000,0);
        assertEquals(Test.getMontoTotalPagado(),(float) 70000,0);
        assertEquals(Test.getEstadoCuota(),"Pagado");
        assertEquals(cuotas.get(0).getEstadoCuota(),"Pagado");
    }
    @Test
    public void ConteoDeCuotasYFechaDeUltimaPagada() {
        //Elementos Internos.
        EstudiantesEntity estudiante = new EstudiantesEntity();   //Estudiante de prueba.
        ArrayList<CuotaEntity> cuotas;  //Cuotas generadas.
        Integer CuotasPagadas;
        Integer CuotasAtradadas;
        String FechaUltimaPagada;

        /*Se genera estudiante de prueba Dummy (esto para evitar errores)*/
        estudiante.setRutEstudiante("prueba2");
        estudiante.setApellidoEstudiante("Ramirez Baeza");
        estudiante.setNombreColegio("Elvio Camba");
        estudiante.setFechaNacimiento(new Date());
        estudiante.setTipoColegio("Privado");
        estudiante.setNombreColegio("Weston Academy");
        estudiante.setEgreso(4);

        /*Generar cuotas y buscar por ID*/
        estudiante = estudiantesService.guardarEstudiantes(estudiante);
        cuotas = cuotaService.generarCuotasEstudiante("prueba2",1,"Contado");
        cuotaRepository.deleteAll(cuotas);
        estudiantesRepository.delete(estudiante);

        /*Conteo de cuotas*/
        CuotasPagadas = cuotaService.ContarCuotasPagadas(cuotas);
        CuotasAtradadas = cuotaService.ContarCuotasAtrasadas(cuotas);
        FechaUltimaPagada = cuotaService.FechaUltimaCuotaPagada(cuotas);

        /*Se verifica resultados*/
        assertEquals(CuotasPagadas,1,0);
        assertEquals(CuotasAtradadas,0,0);
        assertEquals(FechaUltimaPagada, "");
    }


}
