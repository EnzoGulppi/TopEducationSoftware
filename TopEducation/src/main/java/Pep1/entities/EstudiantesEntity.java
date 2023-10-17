package Pep1.entities;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.sql.Date;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idEstudiante;
    private String rutEstudiante;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private Date fechaNacimiento;
    private String tipoColegio;
    private String nombreColegio;
    private Integer egreso;

    @OneToMany(mappedBy = "estudiante")
    private List<CuotaEntity> cuotas;
}
