package Pep1.repositories;

import Pep1.entities.CuotaEntity;
import Pep1.entities.EstudiantesEntity;
import Pep1.services.EstudiantesService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<CuotaEntity, Long> {

    @Query("SELECT cuota FROM CuotaEntity cuota WHERE cuota.idEstudiante = :idEstudiante")
    ArrayList<CuotaEntity> findAllByEstudianteId(@Param("idEstudiante") Long idEstudiante);
    @Query("SELECT cuota FROM CuotaEntity cuota WHERE cuota.idCuota = :idCuota")
    CuotaEntity findByIdCuota(Long idCuota);
    CuotaEntity findPlanillaByEstudiante(EstudiantesEntity estudiantes);
}
