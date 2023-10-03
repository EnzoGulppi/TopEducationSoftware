package Pep1.repositories;

import Pep1.entities.CuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuotaRepository extends JpaRepository<CuotaEntity, Long> {
    List<CuotaEntity> findByEstudianteRutEstudianteAndEsMatricula(String rutEstudnate, boolean esMatricula);
}
