package Pep1.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cuota")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CuotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuota;
    private Long idEstudiante;
    private Double arancel;
    private String tipoPago;
    private Double montoTotalPagado;
    private LocalDate creacionCuota;
    private LocalDate pagoCuota;
    private String estadoCuota;
    private Integer atraso;



    @ManyToOne
    private EstudiantesEntity estudiante;

}
