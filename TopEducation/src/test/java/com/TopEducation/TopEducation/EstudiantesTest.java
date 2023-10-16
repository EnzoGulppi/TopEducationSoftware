package com.TopEducation.TopEducation;

import Pep1.repositories.EstudiantesRepository;
import Pep1.services.EstudiantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EstudiantesTest {
    @Autowired
    private EstudiantesService estudiantesService;

    @Autowired
    private EstudiantesRepository estudiantesRepository;
}
