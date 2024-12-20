package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void testCreateStudent() throws Exception {
        final String name = "Мальвина";
        final int age = 13;
        final Long id = 1l;



        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student testMockStudent = new Student();
        testMockStudent.setId(id);
        testMockStudent.setName(name);
        testMockStudent.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(testMockStudent);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testMockStudent));
        mockMvc.perform(MockMvcRequestBuilders
                .post("/student")
                .content(studentObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher)jsonPath("$.id").value(id))
                .andExpect((ResultMatcher)jsonPath("$.name").value(name))
                .andExpect((ResultMatcher)jsonPath("$.age").value(age));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/student/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher)jsonPath("$.id").value(id))
                .andExpect((ResultMatcher)jsonPath("$.name").value(name))
                .andExpect((ResultMatcher)jsonPath("$.age").value(age));
    }

}
