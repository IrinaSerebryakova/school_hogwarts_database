package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Student;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyApplicationTest {

        @LocalServerPort
        private int port;
        @Autowired
        private FacultyController facultyController;

        @Autowired
        private TestRestTemplate restTemplate;

        @Test
        public void contextLoads() throws Exception{
            Assertions.assertThat(facultyController).isNotNull();
        }

        @Test
        public void testGetFaculty() throws Exception {
            Assertions
                    .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                    .isNotEmpty();
        }


        @Test
        public void testGetFacultyByColor() throws Exception {
            Assertions
                    .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/color", String.class))
                    .isNotEmpty();
        }
    }

