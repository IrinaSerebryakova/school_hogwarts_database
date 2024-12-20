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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FacultyRepository facultyRepository;

    @Spy
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void testCreateFaculty() throws Exception {
        final Long id = 1l;
        final String name = "Когтевра";
        final String color = "Серо-буро-малиновый";


        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty testFaculty = new Faculty();
        testFaculty.setId(id);
        testFaculty.setName(name);
        testFaculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(testFaculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(testFaculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value(id))
                .andExpect((ResultMatcher)jsonPath("$.name").value(name))
                .andExpect((ResultMatcher)jsonPath("$.age").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher)jsonPath("$.id").value(id))
                .andExpect((ResultMatcher)jsonPath("$.name").value(name))
                .andExpect((ResultMatcher)jsonPath("$.age").value(color));
    }

}
