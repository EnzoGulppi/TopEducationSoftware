package Pep1.repositories;

import Pep1.entities.EstudiantesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudiantesRepository extends JpaRepository<EstudiantesEntity, String> {
    @Query("select e from EstudiantesEntity e where e.nombreEstudiante = :nombre")
    EstudiantesEntity findByNameCustomQuery(@Param("nombre") String nombreEstudiante);
}
