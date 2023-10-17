package Pep1;

import Pep1.entities.EstudiantesEntity;
import Pep1.repositories.EstudiantesRepository;
import Pep1.services.EstudiantesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.Assert.*;

@SpringBootTest
public class EstudiantesTest {
    @Autowired
    private EstudiantesService estudiantesService;

    @Autowired
    private EstudiantesRepository estudiantesRepository;

}
