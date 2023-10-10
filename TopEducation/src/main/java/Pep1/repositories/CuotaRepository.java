package Pep1.repositories;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.services.EstudiantesService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuotaRepository extends JpaRepository<CuotaEntity, Long> {

    CuotaEntity findPlanillaByEstudiante(EstudiantesEntity estudiantes);
}
