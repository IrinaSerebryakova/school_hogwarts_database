package ru.hogwarts.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.hogwarts.school.model.Student;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public interface StudentService {
    Student createStudent(Student student);

    Student getStudentById(long id);

    String deleteStudent(Long id);

    Student updateStudent(Student student);

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findAll();

    List<Double> getAverageAge();

    List<Integer> getCountOfStudents();

    List<Student> getFiveLastStudents();

    List<Student> getStudentsByName(String name);

   List<String> getStudentsByNameStartsWithLetter(String letter);
}
