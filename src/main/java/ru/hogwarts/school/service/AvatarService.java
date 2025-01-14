package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentService studentService;

    @Autowired
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long id, MultipartFile avatarFile) throws IOException {
        logger.info("The method 'uploadAvatar' was called");
        System.out.println(1);
        Student student = studentService.getStudentById(id);
        System.out.println(2);
        Path filePath = Path.of(avatarsDir,
                student + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
            Avatar avatar = findAvatar(id);

            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatarRepository.save(avatar);
            System.out.println(3);
        } catch (IOException e) {
            System.out.println(2.5);
            logger.warn("The method 'uploadAvatar' throws IOException");
            logger.debug("Please, re-run your application with 'debug'");
            throw new IOException("Wrong data");
        }
    }

    private byte[] findAvatarLocal(Long id) throws IOException {
        logger.info("The method 'findAvatarLocal' was called");
        if(avatarRepository.findAvatarByStudentId(id) == null){
            logger.warn("The method 'findAvatarLocal' throws AvatarNotFoundException");
            logger.debug("Please, re-run your application with 'debug'");
        }
        Avatar avatar = avatarRepository.findAvatarByStudentId(id)
                .orElseThrow(AvatarNotFoundException::new);
        return Files.readAllBytes(Path.of(avatar.getFilePath()));
    }

    public Avatar findAvatar(Long id) {
        logger.info("The method 'findAvatar' was called");
        if(avatarRepository.findAvatarByStudentId(id) == null){
            logger.warn("The method 'findAvatarLocal' throws AvatarNotFoundException");
            logger.debug("Please, re-run your application with 'debug'");
        }
        Avatar avatar = avatarRepository.findAvatarByStudentId(id).orElseThrow(AvatarNotFoundException::new);
        System.out.println(avatar);
        return avatar;
    }

    public String getExtensions(String fileName) {
        logger.info("The method 'getExtensions' was called");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    public List<Avatar> getAvatarsListFromPage(int pageNumber, int pageSize) {
        logger.info("The method 'getAvatarsListFromPage' was called");
        Page<Avatar> pageOfAvatar = avatarRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return pageOfAvatar.hasContent() ? pageOfAvatar.getContent() : Collections.emptyList();
    }
}
