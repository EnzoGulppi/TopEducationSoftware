package Pep1;

import Pep1.services.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ReporteTest {

    @Autowired
    private ReporteService reporteService;

    @Test
    public void testResumenEstadoDePagos() {
        ResponseEntity<byte[]> response = reporteService.ResumenEstadoDePagos();

        // Asegúrate de que el servicio responde con un estado HTTP 200 (OK)
        assertEquals(200, response.getStatusCodeValue());

        // Puedes realizar más aserciones según tus necesidades
    }
}
