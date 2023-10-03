package Pep1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "data")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubirDataEntity {
    @Id
    @NotNull
    private String rutEstudiante ;
    private String fechaExamen;
    private String puntajeExamen;


}
