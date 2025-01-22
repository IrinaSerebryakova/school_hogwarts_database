
package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.*;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentServiceProduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentServiceProduction studentServiceProduction;

    @SpyBean
    private AvatarService avatarService;
    
    private static final Long NOT_EXIST_ID = 5L;
    private static final Long STUDENT_ID = 5L;
    private static final String STUDENT_NAME = "Мальвина";
    private static final int STUDENT_AGE = 13;
    JSONObject jsonObject = new JSONObject();

    @BeforeEach
    public void initData() throws Exception {
        jsonObject.put("id", 5L);
        jsonObject.put("name", "Мальвина");
        jsonObject.put("age", 13);
    }

    @Test
    public void testSaveStudent() throws Exception {
        Student testStudent = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student/add")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
    }

    @Test
    public void testFindAllStudents() throws Exception {
        Student testStudent = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        when(studentServiceProduction.findAll()).thenReturn(List.of(testStudent));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAllStudentsIfDataBaseIsEmpty() throws Exception {
        List<Student> emptyList = new ArrayList<>();
        when(studentRepository.findAll()).thenReturn(emptyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/all")
                        .content(emptyList.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student testStudent = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + STUDENT_ID)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
    }

    @Test
    public void testGetStudentByIdAndStudentIsNotFound() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + NOT_EXIST_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        JSONObject updateJsonStudent = new JSONObject();
        updateJsonStudent.put("id", STUDENT_ID);
        updateJsonStudent.put("name", "Валентина");
        updateJsonStudent.put("age", 16);

        Student updateMockStudent = new Student(STUDENT_ID, "Валентина", 16);

        when(studentRepository.save(any(Student.class))).thenReturn(updateMockStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(updateJsonStudent.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value("Валентина"))
                .andExpect(jsonPath("$.age").value(16));
    }

    @Test
    public void testSaveStudentAndGet404Error() throws Exception {
        Student newStudent = new Student(0L, null, 0);
        when(studentServiceProduction.createStudent(newStudent)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newStudent.toString()))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeleteStudent() throws Exception {
        Student testStudent = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE);

        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        when(studentServiceProduction.deleteStudent(STUDENT_ID)).thenReturn(String.valueOf(testStudent));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + STUDENT_ID)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testThatStudentForDeleteIsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/", STUDENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAverageAge() throws Exception {
        List<Long> averageAge = List.of(13L);

        when(studentServiceProduction.getAverageAge()).thenReturn(averageAge);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/average-age"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCountOfStudents() throws Exception {
        List<Integer> countOfStudents = List.of(8);

        when(studentRepository.getCountOfStudents()).thenReturn(countOfStudents);
        when(studentServiceProduction.getCountOfStudents()).thenReturn(countOfStudents);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/count"))
                .andExpect(status().isOk());
    }

    Student testStudent = new Student(0L, STUDENT_NAME, STUDENT_AGE);
    Student testStudent1 = new Student(1L, STUDENT_NAME, STUDENT_AGE);
    Student testStudent2 = new Student(2L, STUDENT_NAME, STUDENT_AGE);
    Student testStudent3 = new Student(3L, STUDENT_NAME, STUDENT_AGE);
    Student testStudent4 = new Student(4L, STUDENT_NAME, STUDENT_AGE);
    Student testStudent5 = new Student(5L, STUDENT_NAME, STUDENT_AGE);


    @Test
    public void testGetFiveLastStudents() throws Exception {
        List<Student> fiveLastStudents = List.of(testStudent1, testStudent2, testStudent3, testStudent4, testStudent5);

        when(studentRepository.getFiveLastStudents()).thenReturn(fiveLastStudents);
        when(studentServiceProduction.getFiveLastStudents()).thenReturn(fiveLastStudents);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/last"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudentsByNameInParallelMode() throws Exception {
        List<Student> allStudents = List.of(testStudent, testStudent1, testStudent2, testStudent3, testStudent4, testStudent5);

        when(studentRepository.findAll()).thenReturn(allStudents);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/students/print-parallel"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudentsByNameInSynchronizedMode() throws Exception {
        List<Student> allStudents = List.of(testStudent, testStudent1, testStudent2, testStudent3, testStudent4, testStudent5);

        when(studentRepository.findAll()).thenReturn(allStudents);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/students/print-synchronized"))
                .andExpect(status().isOk());
    }
}

