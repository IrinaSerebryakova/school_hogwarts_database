package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.exception.StudentNotFoundException;
import java.util.*;

@Service
@Transactional
@Profile("production")
public class StudentServiceProduction implements StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceProduction.class);

    @Autowired
    private StudentRepository studentRepository;

    public StudentServiceProduction(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentServiceProduction() {
    }

    public Student createStudent(Student student) {
        logger.info("The method 'createStudent' was called");
        return studentRepository.save(student);
    }

    public Student getStudentById(long id) {
        logger.info("The method 'getStudentById' was called");
        if(studentRepository.findById(id) == null){
            logger.warn("The method 'findAvatarLocal' throws StudentNotFoundException");
            logger.debug("Please, re-run your application with 'debug'");
        }
        return studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    public String deleteStudent(Long id) {
        logger.info("The method 'deleteStudent' was called");
        String deletedStudent = String.valueOf(studentRepository.findById(id));
        studentRepository.deleteById(id);
        return deletedStudent;
    }

    public Student updateStudent(Student student) {
        logger.info("The method 'updateStudent' was called");
        return studentRepository.save(student);
    }

    public List<Student> findByAge(int age) {
        logger.info("The method 'findByAge' was called");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("The method 'findByAgeBetween' was called");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> findAll() {
        logger.info("The method 'findAll' was called");
        return studentRepository.findAll();
    }

    public List<Integer> getAverageAge() {
        logger.info("The method 'getAverageAge' was called");
        return studentRepository.getAverageAge();
    }

    public List<Integer> getCountOfStudents() {
        logger.info("The method 'getCountOfStudents' was called");
        return studentRepository.getCountOfStudents();
    }

    public List<Student> getFiveLastStudents() {
        logger.info("The method 'getFiveLastStudents' was called");
        return studentRepository.getFiveLastStudents();
    }

    public List<Student> getStudentsByName(String name) {
        logger.info("The method 'getStudentsByName' was called");
        return studentRepository.getStudentsByName(name);
    }
}
