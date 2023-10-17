package Pep1.services;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.entities.SubirDataEntity;
import com.opencsv.CSVWriter;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {
    @Autowired
    private CuotaService cuotaService;

    @Autowired
    EstudiantesService estudiantesService;

    @Autowired
    SubirDataService pruebaService;

    @Generated
    public ResponseEntity<byte[]> ArchivoPlannillaAranceles() {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"Rut", "Valor Arancel", "Tipo de pago", "Descuento Colegio",
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

    public String obtenerDescuentoPorTipoColegio(String tipoColegio) {
        switch (tipoColegio) {
            case "Municipal":
                return "20%";
            case "Subvencionado":
                return "10%";
            default:
                return "0%";
        }
    }

    public String obtenerDescuentoPorAniosEgreso(int aniosEgreso) {
        if (aniosEgreso == 0) {
            return "15%";
        } else if (aniosEgreso <= 2) {
            return "8%";
        } else if (aniosEgreso <= 4) {
            return "4%";
        }
        return "0%";
    }

    public String obtenerDescuentoPorPruebas(int promedioPruebas) {
        if (promedioPruebas >= 950) {
            return "10%";
        } else if (promedioPruebas >= 900) {
            return "5%";
        } else if (promedioPruebas >= 850) {
            return "2%";
        }
        return "0%";
    }

    public void actualizarPrecioCuotas(List<CuotaEntity> cuotas, float descuentoPuntaje) {
        for (CuotaEntity cuota : cuotas) {
            double montoPagado = (float) (cuota.getMontoTotalPagado() - (cuota.getArancel() * descuentoPuntaje));
            cuota.setMontoTotalPagado(montoPagado);
        }
        cuotaService.ActualizarCuotas((ArrayList<CuotaEntity>) cuotas);
    }
    @Generated
    public ResponseEntity<byte[]> ResumenEstadoDePagos() {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"Rut", "Nombre de estudiante", "Nro Examenes rendidos", "Promedio puntaje exámenes",
                "Monto total arancel a pagar", "Tipo Pago (Contado/Cuotas)",
                "Nro. total de cuotas pactadas", "Nro. cuotas pagadas", "Monto total pagado",
                "Fecha último pago", "Saldo por pagar", "Nro. Cuotas con retraso"});

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        List<EstudiantesEntity> estudiantes = estudiantesService.obtenerEstudiantes();

        for (EstudiantesEntity estudiante : estudiantes) {
            String RutEstudiante = estudiante.getRutEstudiante();
            String NombreEstudiante = estudiante.getNombreEstudiante() + " " + estudiante.getApellidoEstudiante();

            ArrayList<CuotaEntity> cuotasEstudiante = cuotaService.obtenerCuotasPorRutEstudiante(RutEstudiante);
            ArrayList<SubirDataEntity> pruebasEstudiante = pruebaService.ObtenerPruebasPorRutEstudiante(RutEstudiante);

            String NroExamenesRendidos = String.valueOf(pruebasEstudiante.size());
            String NroTotalCuotas = String.valueOf(cuotasEstudiante.size());
            String Promedio = String.valueOf(pruebaService.PromediosPruebasEstudiante(pruebasEstudiante));
            String Monto = "";
            String TipoPago = "";
            String TotalPagadas = "";
            String MontoPagado = "";
            String CuotasAtrasadas = "";
            String FechaUltimoPago = "";
            String MontoPorPagar = "";

            if (!cuotasEstudiante.isEmpty()) {
                TipoPago = cuotasEstudiante.get(0).getTipoPago();
                Monto = String.valueOf((cuotasEstudiante.get(0).getMontoTotalPagado())
                        * (cuotasEstudiante.size() - 1));

                int CuotasPagadas = cuotaService.ContarCuotasPagadas(cuotasEstudiante);
                TotalPagadas = String.valueOf(CuotasPagadas - 1);
                MontoPagado = String.valueOf((cuotasEstudiante.get(0).getMontoTotalPagado()) * CuotasPagadas);

                MontoPorPagar = String.valueOf((cuotasEstudiante.get(0).getMontoTotalPagado())
                        * (cuotasEstudiante.size() - CuotasPagadas));
                CuotasAtrasadas = String.valueOf(cuotaService.ContarCuotasAtrasadas(cuotasEstudiante));
                FechaUltimoPago = cuotaService.FechaUltimaCuotaPagada(cuotasEstudiante);
            }

            data.add(new String[]{RutEstudiante, NombreEstudiante, NroExamenesRendidos, Promedio,
                    Monto, TipoPago, NroTotalCuotas, TotalPagadas, MontoPagado,
                    FechaUltimoPago, MontoPorPagar, CuotasAtrasadas});
        }

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "Reporte.csv");

        return ResponseEntity.ok().headers(headers).contentLength(outputStream.size()).body(outputStream.toByteArray());
    }


}
