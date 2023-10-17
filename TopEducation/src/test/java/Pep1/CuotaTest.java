package Pep1;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.CuotaRepository;
import Pep1.repositories.EstudiantesRepository;
import Pep1.services.CuotaService;
import Pep1.services.EstudiantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class CuotaTest {
    @Autowired
    private CuotaService cuotaService;

    @Autowired
    private EstudiantesService estudiantesService;

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private EstudiantesRepository estudiantesRepository;


    @Test
    void ListarCuotas_RutNoExiste() {
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
    public void testContarCuotasPagadas() {
        // Crear una lista de CuotaEntity simulada con algunas cuotas pagadas
        ArrayList<CuotaEntity> cuotas = new ArrayList<>();
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setEstadoCuota("Pagado");
        cuotas.add(cuota1);

        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setEstadoCuota("Pagado");
        cuotas.add(cuota2);

        CuotaEntity cuota3 = new CuotaEntity();
        cuota3.setEstadoCuota("Pendiente");
        cuotas.add(cuota3);

        // Llamar al método que queremos probar
        Integer resultado = cuotaService.ContarCuotasPagadas(cuotas);

        // Verificar que el resultado sea igual al número de cuotas pagadas
        assertEquals(2, resultado);
    }



    @Test
    void BuscarCuotaPorID(){
        //Elementos Internos.
        EstudiantesEntity estudiante = new EstudiantesEntity();   //Estudiante de prueba.
        ArrayList<CuotaEntity> cuotas;  //Cuotas generadas.
        CuotaEntity Test;       //Cuota de prueba

        /*Se genera estudiante de prueba Dummy (esto para evitar errores)*/
        estudiante.setRutEstudiante("prueba2");
        estudiante.setApellidoEstudiante("Ramirez Baeza");
        estudiante.setNombreEstudiante("Elvio Camba");
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
