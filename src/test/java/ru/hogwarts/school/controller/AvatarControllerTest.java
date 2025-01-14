package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentServiceProduction;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvatarController.class)
public class AvatarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private AvatarService avatarService;

    @SpyBean
    private StudentServiceProduction studentServiceProduction;

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Long STUDENT_ID = 1L;
    private final static String AVATAR_NAME = "photo";
    private final static String ORIGINAL_FILE_NAME = "photo.jpg";
    private final static byte[] AVATAR_DATA = "test".getBytes();
    private final static String MEDIA_TYPE = "image/jpeg";
    private final static String FILE_PATH = "/avatars/photo.jpg"; // Замените на реальный или фиктивный путь
    private final static long FILE_SIZE = AVATAR_DATA.length;

    private final static String STUDENT_NAME = "Мальвина";
    private final static int STUDENT_AGE = 13;


    @Test
    public void testUploadAvatar() throws Exception {
        Student testStudent = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE);
        MockMultipartFile avatar = new MockMultipartFile(AVATAR_NAME, ORIGINAL_FILE_NAME, MEDIA_TYPE, AVATAR_DATA);
        Avatar expectedAvatar = new Avatar(1L, FILE_PATH, FILE_SIZE, MEDIA_TYPE, testStudent);

        when(avatarRepository.save(any(Avatar.class))).thenReturn(expectedAvatar);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));
        when(avatarRepository.findAvatarByStudentId(any())).thenReturn(Optional.of(expectedAvatar));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/avatar/" + STUDENT_ID)
                        .file("avatar", avatar.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(studentServiceProduction, times(1)).getStudentById(STUDENT_ID);
        verify(avatarService, times(1)).uploadAvatar(eq(STUDENT_ID), any(MultipartFile.class));
    }

    @Test
    public void uploadAvatar_studentNotFound() throws Exception {
        MockMultipartFile avatar = new MockMultipartFile(AVATAR_NAME, ORIGINAL_FILE_NAME, MEDIA_TYPE, AVATAR_DATA);

        when(studentRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/avatar/" + STUDENT_ID)
                        .file("avatar", avatar.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());

        verify(avatarRepository, never()).save(any());
    }

    @Test
    public void downloadAvatar_fromDb_success() throws Exception {
        Avatar avatar = new Avatar(1L, FILE_PATH, FILE_SIZE, MEDIA_TYPE, new Student(STUDENT_ID, "Test", 18));
        avatar.setData(AVATAR_DATA); //не устанавливается в контроллере
        when(avatarRepository.findAvatarByStudentId(any())).thenReturn(Optional.of(avatar));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/avatar/" + STUDENT_ID + "/db"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(content().bytes(AVATAR_DATA));
    }

    @Test
    public void downloadAvatar_fromDb_notFound() throws Exception {
        when(avatarRepository.findAvatarById(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/avatar/" + 15L + "/db?"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void downloadAvatar_fromFile_success() throws Exception {
        Avatar avatar = new Avatar(1L, FILE_PATH, FILE_SIZE, MEDIA_TYPE, new Student(STUDENT_ID, "Test", 18));
        when(avatarRepository.findAvatarByStudentId(STUDENT_ID)).thenReturn(Optional.of(avatar));

        mockMvc.perform(MockMvcRequestBuilders.get("/avatar/" + STUDENT_ID + "/file"))
                .andExpect(status().isOk());
    }

    @Test
    public void downloadAvatar_fromFile_notFound() throws Exception {
        when(avatarRepository.findAvatarByStudentId(STUDENT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/avatar/" + STUDENT_ID + "/file"))
                .andExpect(status().isNotFound());
    }

}
