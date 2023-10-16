package Pep1.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "data")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubirDataEntity {
    @Id
    @NotNull
    private Long idPrueba;

    private Long idEstudiante;
    private Integer puntaje;
    private LocalDate fechaRealizada;
    private LocalDate fechaResultado;


}
