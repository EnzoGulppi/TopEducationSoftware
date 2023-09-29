package Pep1.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "estudiantes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstudiantesEntity {
    @Id
    @NotNull
    private String rutEstudiante;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private LocalDate fechaNacimiento;
    private String tipoColegio;
    private String nombreColegio;
    private Integer egreso;

    @OneToMany(mappedBy = "estudiante")
    private List<CuotaEntity> cuotas;
}
