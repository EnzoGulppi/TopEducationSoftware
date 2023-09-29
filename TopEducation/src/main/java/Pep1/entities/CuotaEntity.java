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
    private BigDecimal monto;
    private LocalDate vencimiento;
    private String estado = "Pendiente";

    @ManyToOne
    private EstudiantesEntity estudiante;
}
