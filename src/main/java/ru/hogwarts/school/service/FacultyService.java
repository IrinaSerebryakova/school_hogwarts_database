package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.util.PropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Comparator;
import java.util.List;


@Service
@Transactional
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    private FacultyRepository facultyRepository;

    public FacultyService() {
    }

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("The method 'createFaculty' was called");
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        logger.info("The method 'getFacultyById' was called");
        facultyRepository.findById(id);
        return facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }


    public void deleteFaculty(Long id) {
        logger.info("The method 'deleteFaculty' was called");
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColorIgnoreCase(String color) {
        logger.info("The method 'findByColorIgnoreCase' was called");
        if(facultyRepository.findByColorIgnoreCase(color) == null){
            logger.warn("The method 'findByColorIgnoreCase' throws FacultyNotFoundException");
            logger.debug("Please, re-run your application with 'debug'");
            throw new FacultyNotFoundException();
        }
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("The method 'updateFaculty' was called");
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findAll() {
        logger.info("The method 'findAll' was called");
        return facultyRepository.findAll();
    }

    public String getTheLongestNameOfFaculty() {
        logger.info("The method 'getTheLongestNameOfFaculty' was called");
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse("");
    }
}
