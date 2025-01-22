package ru.hogwarts.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add")
    public Student createStudent(@Valid @NotNull @RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> findAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentsByAge(@RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(emptyList());
    }

    @GetMapping("/age")
    public ResponseEntity<Collection<Student>> findByAgeBetweenMinAndMax(@RequestParam int minAge, @RequestParam int maxAge) {
        if (minAge > 0 && maxAge > 0) {
            return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
        }
        return ResponseEntity.ok(emptyList());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student student1 = studentService.updateStudent(student);
        if (student1 == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student1);
    }

    @GetMapping("/average-age")
    public ResponseEntity<List<Long>> getAverageAge() {
        if (studentService.findAll() == null) {
            return ResponseEntity.ok(emptyList());
        }
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("/count")
    public ResponseEntity<List<Integer>> getCountOfStudents() {
        if (studentService.findAll() != null) {
            return ResponseEntity.ok(studentService.getCountOfStudents());
        }
        return ResponseEntity.ok(emptyList());
    }

    @GetMapping("/last")
    public ResponseEntity<List<Student>> getFiveLastStudents() {
        if (studentService.findAll() != null) {
            return ResponseEntity.ok(studentService.getFiveLastStudents());
        }
        return ResponseEntity.ok(emptyList());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Student>> getStudentsByName(@PathVariable("name") String name) {
        List<Student> students = studentService.getStudentsByName(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/name/{letter}")
    public ResponseEntity<List<String>> getStudentsByNameStartsWithLetter(@PathVariable("letter") String letter) {
        if (letter.length() != 1 && studentService.findAll() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentService.getStudentsByNameStartsWithLetter(letter));
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<List<Student>> getStudentsByNameInParallelMode() {
        if (studentService.findAll() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentService.getStudentsByNameInParallelMode());
    }

    @GetMapping("/print-synchronized")
    public ResponseEntity<List<Student>> getStudentsByNameInSynchronizedMode() {
        if (studentService.findAll() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentService.getStudentsByNameInSynchronizedMode());
    }
}
