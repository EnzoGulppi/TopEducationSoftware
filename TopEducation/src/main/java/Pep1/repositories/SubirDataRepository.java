package Pep1.repositories;

import Pep1.entities.SubirDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface SubirDataRepository extends JpaRepository<SubirDataEntity, String> {
    @Query("SELECT prueba FROM SubirDataEntity prueba WHERE prueba.idEstudiante = :idEstudiante")
    ArrayList<SubirDataEntity> findAllByEstudianteId(@Param("idEstudiante") Long idEstudiante);
}
