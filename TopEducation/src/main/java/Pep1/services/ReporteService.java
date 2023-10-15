package Pep1.services;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.entities.SubirDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.annotation.processing.Generated;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {
    @Autowired
    CuotaService cuotaService;

    @Autowired
    EstudiantesService estudiantesService;

    @Autowired
    SubirDataService pruebaService;

    @Generated()
    public ResponseEntity<byte[]> ArchivoPlannillaAranceles() {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"Rut", "Valor Nominal", "Tipo de pago", "Descuento Colegio",
                "Descuento egreso", "Descuento pruebas", "Descuento Tipo Pago", "Total a pagar"});

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArrayList<EstudiantesEntity> estudiantes = estudiantesService.obtenerEstudiantes();

        for (EstudiantesEntity estudiante : estudiantes) {
            String RutEstudiante = estudiante.getRutEstudiante();
            String TipoPago = "";
            String DescuentoPorTipoColegio = "0%";
            String DescuentoPorAniosEgreso = "0%";
            String DescuentoPorPruebas = "0%";
            Float descuentoPuntaje = 0.0F;

            List<CuotaEntity> cuotasEstudiante = cuotaService.obtenerCuotasPorRutEstudiante(RutEstudiante);
            //List<SubirDataEntity> pruebasEstudiante = pruebaService.obtenerCuotasPorRutEstudiante(RutEstudiante);

            if (!cuotasEstudiante.isEmpty()) {
                TipoPago = cuotasEstudiante.get(0).getTipoPago();

                if (!"Contado".equals(TipoPago)) {
                    DescuentoPorTipoColegio = obtenerDescuentoPorTipoColegio(estudiante.getTipoColegio());
                    DescuentoPorAniosEgreso = obtenerDescuentoPorAniosEgreso(estudiante.getEgreso());
                    //DescuentoPorPruebas = obtenerDescuentoPorPruebas(pruebaService.PromediosPruebasEstudiante(pruebasEstudiante));

                    actualizarPrecioCuotas(cuotasEstudiante, descuentoPuntaje);

                    String total = String.valueOf((cuotasEstudiante.get(1).getMontoTotalPagado())
                            * (cuotasEstudiante.size() - 1));

                    data.add(new String[]{RutEstudiante, "1500000", TipoPago, DescuentoPorTipoColegio,
                            DescuentoPorAniosEgreso, DescuentoPorPruebas, "0%", total});
                }
            }
        }

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "PlanillaAranceles.csv");

        return ResponseEntity.ok().headers(headers).contentLength(outputStream.size()).body(outputStream.toByteArray());
    }

    private String obtenerDescuentoPorTipoColegio(String tipoColegio) {
        switch (tipoColegio) {
            case "Municipal":
                return "20%";
            case "Subvencionado":
                return "10%";
            default:
                return "0%";
        }
    }

    private String obtenerDescuentoPorAniosEgreso(int aniosEgreso) {
        if (aniosEgreso == 0) {
            return "15%";
        } else if (aniosEgreso <= 2) {
            return "8%";
        } else if (aniosEgreso <= 4) {
            return "4%";
        }
        return "0%";
    }

    private String obtenerDescuentoPorPruebas(int promedioPruebas) {
        if (promedioPruebas >= 950) {
            return "10%";
        } else if (promedioPruebas >= 900) {
            return "5%";
        } else if (promedioPruebas >= 850) {
            return "2%";
        }
        return "0%";
    }

    private void actualizarPrecioCuotas(List<CuotaEntity> cuotas, float descuentoPuntaje) {
        for (CuotaEntity cuota : cuotas) {
            double montoPagado = (float) (cuota.getMontoTotalPagado() - (cuota.getArancel() * descuentoPuntaje));
            cuota.setMontoTotalPagado(montoPagado);
        }
        cuotaService.ActualizarCuotas((ArrayList<CuotaEntity>) cuotas);
    }

}
