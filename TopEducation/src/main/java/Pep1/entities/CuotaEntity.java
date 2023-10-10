package Pep1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuota")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CuotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double arancel;
    private String tipoPago;
    private Integer numeroCoutas;
    private Double porPagar;
    private LocalDate vencimiento;
    private String rutEstudiante;



    @ManyToOne
    private EstudiantesEntity estudiante;

}
