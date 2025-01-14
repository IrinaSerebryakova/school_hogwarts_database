package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(Student student);

    Student getStudentById(long id);

    String deleteStudent(Long id);

    Student updateStudent(Student student);

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findAll();

    List<Integer> getAverageAge();

    List<Integer> getCountOfStudents();

    List<Student> getFiveLastStudents();

    List<Student> getStudentsByName(String name);
}
