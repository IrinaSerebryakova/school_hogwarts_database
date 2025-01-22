package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        if (studentRepository.findById(id).isEmpty()) {
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

    public List<Long> getAverageAge() {
        logger.info("The method 'getAverageAge' was called");
        List<Student> allStudents = studentRepository.findAll();
        Long averageAge = (long) allStudents.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
        return List.of(averageAge);
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

    public List<String> getStudentsByNameStartsWithLetter(String letter) {
        logger.info("The method 'getStudentsByNameStartsWithLetter' {} was called", letter);
        return studentRepository.findAll().stream()
                .filter(student -> student.getName() != null && student.getName().startsWith(letter))
                .map(student -> student.getName().toUpperCase())
                .sorted(Comparator.naturalOrder())
                .collect((Collectors.toList()));
    }


    int counter = 0;
    int[] count = {1, 1, 2, 2, 3, 3};

    public void printName(String name) {
        System.out.println(name);
    }

    public List<Student> getStudentsByNameInParallelMode() {
        logger.info("The method 'getStudentsByNameInParallelMode' was called");
        long start = System.currentTimeMillis();
        List<Student> allStudentsForThreads = studentRepository.findAll();
        counter = -1;

        StudentServiceProduction ssp = new StudentServiceProduction();
        ssp.printName("thread1 : " + allStudentsForThreads.get(++counter).getName());
        ssp.printName("thread1 : " + allStudentsForThreads.get(++counter).getName());

        new Thread(() -> {
            ssp.printName("thread2 : " + allStudentsForThreads.get(++counter).getName());
            ssp.printName("thread2 : " + allStudentsForThreads.get(++counter).getName());
        }).start();

        new Thread(() -> {
            ssp.printName("thread3 : " + allStudentsForThreads.get(++counter).getName());
            ssp.printName("thread3 : " + allStudentsForThreads.get(++counter).getName());
        }).start();
        long finish = System.currentTimeMillis();
        logger.info("The method 'getStudentsByNameInParallelMode' was running: {}", finish);
        return Collections.emptyList();
    }

    public final Object lock = new Object();

    public void printSynchronized(String name) {
        synchronized (lock) {
            System.out.println(name);
        }
    }

    public List<Student> getStudentsByNameInSynchronizedMode() {
        logger.info("The method 'getStudentsByNameInSynchronizedMode' was called");
        long start = System.currentTimeMillis();
        List<Student> allStudentsForThreads = studentRepository.findAll();
        counter = -1;

        StudentServiceProduction ssp = new StudentServiceProduction();
        ssp.printSynchronized("thread1 : " + allStudentsForThreads.get(++counter).getName());
        ssp.printSynchronized("thread1 : " + allStudentsForThreads.get(++counter).getName());

        new Thread(() -> {
            ssp.printSynchronized("thread2 : " + allStudentsForThreads.get(++counter).getName());
            ssp.printSynchronized("thread2 : " + allStudentsForThreads.get(++counter).getName());
        }).start();

        new Thread(() -> {
            ssp.printSynchronized("thread3 : " + allStudentsForThreads.get(++counter).getName());
            ssp.printSynchronized("thread3 : " + allStudentsForThreads.get(++counter).getName());
        }).start();
        long finish = System.currentTimeMillis();
        logger.info("The method 'getStudentsByNameInSynchronizedMode' was running: {}", finish);

        return Collections.emptyList();
    }
}
