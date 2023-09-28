package Pep1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "estudiantes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstudiantesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long idEstudiante;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private String rutEstudiante;
    private String fechaNacimiento;
    private String tipoColegio;
    private String nombreColegio;
    private Integer añoEgreso;
}
