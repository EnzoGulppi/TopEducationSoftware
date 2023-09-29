package Pep1.repositories;

import Pep1.entities.EstudiantesEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudiantesRepository extends JpaRepository<EstudiantesEntity, String> {
    @Query("select e from EstudiantesEntity e where e.nombreEstudiante = :nombreEstudiante")
    EstudiantesEntity findByNameCustomQuery(@Param("nombreEstudiante") String nombreEstudiante);

    @Query("select e from EstudiantesEntity e where e.rutEstudiante = :rutEstudiante")
    EstudiantesEntity findByRutEstudiante(@Param("rutEstudiante") String rutEstudiante);
}
