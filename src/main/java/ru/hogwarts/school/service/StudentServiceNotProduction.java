package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Service
@Transactional
@Profile("test")
public class StudentServiceNotProduction implements StudentService {
    private static final Logger logger = LoggerFactory.getLogger(ru.hogwarts.school.service.StudentServiceNotProduction.class);

    @Autowired
    private RestTemplate restTemplate;
    public StudentServiceNotProduction() {
    }
@Value("${student.url}")
private String studentUrl;

    @Override
    public Student createStudent(Student student) {
        return null;
    }

    @Override
    public Student getStudentById(long id) {
        logger.info("The method 'getStudentById' was called");
        return restTemplate
                .exchange(studentUrl,
                        HttpMethod.GET,
                        new HttpEntity<>(HttpHeaders.EMPTY),
                        Student.class, id
                ).getBody();
    }

    @Override
    public String deleteStudent(Long id) {
        return "";
    }

    @Override
    public Student updateStudent(Student student) {
        return null;
    }

    @Override
    public List<Student> findByAge(int age) {
        return List.of();
    }

    @Override
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return List.of();
    }

    @Override
    public List<Student> findAll() {
        return List.of();
    }

    @Override
    public List<Integer> getAverageAge() {
        return List.of();
    }

    @Override
    public List<Integer> getCountOfStudents() {
        return List.of();
    }

    @Override
    public List<Student> getFiveLastStudents() {
        return List.of();
    }

    @Override
    public List<Student> getStudentsByName(String name) {
        return List.of();
    }
}

