package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;


    private static final Long FACULTY_ID = 1L;
    private static final String FACULTY_NAME = "Гриффиндор";
    private static final String FACULTY_COLOR = "красный";
    JSONObject jsonObject = new JSONObject();

    @BeforeEach
    public void initData() throws Exception {
        jsonObject.put("facultyid", FACULTY_ID);
        jsonObject.put("name", FACULTY_NAME);
        jsonObject.put("color", FACULTY_COLOR);
    }

    @AfterEach
    public void clearDB(){
        facultyRepository.deleteAll();
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty expectedFaculty = new Faculty(FACULTY_ID, FACULTY_NAME, FACULTY_COLOR);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(expectedFaculty);

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyid").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    public void testGetFacultyByColor() throws Exception {
        String color = "красный";
        Faculty expectedFaculty = new Faculty(FACULTY_ID, FACULTY_NAME, color);

        when(facultyRepository.findByColorIgnoreCase(color)).thenReturn(List.of(expectedFaculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color?color=" + color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyId").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testGetFacultyByNotExistColor() throws Exception {
        String nonExistentColor = "NonExistentColor";
        when(facultyRepository.findByColorIgnoreCase(nonExistentColor)).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color?color=" + nonExistentColor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetFacultyById() throws Exception {
        long facultyId = 1L;
        Faculty expectedFaculty = new Faculty(facultyId, FACULTY_NAME, FACULTY_COLOR);
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(expectedFaculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + facultyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyid").value(facultyId))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }
}
