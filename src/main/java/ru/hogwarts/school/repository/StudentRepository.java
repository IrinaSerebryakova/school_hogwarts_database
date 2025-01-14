package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT * FROM students OFFSET 3", nativeQuery = true)
    List<Student> getFiveLastStudents();

    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    List<Integer> getCountOfStudents();

    @Query(value = "SELECT AVG(age) FROM students", nativeQuery = true)
    List<Integer> getAverageAge();

    List<Student> getStudentsByName(String name);
}
