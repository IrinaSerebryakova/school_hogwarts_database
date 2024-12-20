package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
	public void contextLoads() throws Exception{
	Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetDefaultMessage() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotEmpty();
    }


    @Test
    public void testGetStudentById() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/1", String.class))
                .isNotEmpty();
    }


	@Test
	public void testFindAllStudents() throws Exception {
		Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/all", String.class))
                .isNotEmpty();
    }
    @Test
    public void testGetFindByAge() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/age", String.class))
                .isNotEmpty();
    }

    @Test
    public void testCreateStudent() throws Exception {
       Student testStudent = new Student();
        testStudent.setName("Неолина");
        testStudent.setAge(14);
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", testStudent, String.class))
                .isNotNull();
    }

}
