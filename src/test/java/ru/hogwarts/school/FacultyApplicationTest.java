package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

@SpringBootTest(classes = SchoolApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyApplicationTest {

    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testControllerIsNotNull() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testGetFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                .isNotNull();
    }

    @Test
    public void testGetFacultyByColor() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/color", String.class))
                .isNotEmpty();
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Когтевран");
        faculty.setColor("Серо-буро-малиновый");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty/create", faculty, String.class
                )).isNotNull();
    }
}
