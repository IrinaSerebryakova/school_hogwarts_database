package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;

@RestController
@RequestMapping
public class AvatarController {
    private final AvatarService avatarService;
    private final StudentService studentServiceProduction;

    public AvatarController(AvatarService avatarService, StudentService studentServiceProduction) {
        this.avatarService = avatarService;
        this.studentServiceProduction = studentServiceProduction;
    }

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id,
                                               @RequestParam MultipartFile avatar) throws IOException {
        System.out.println(avatar);
        if (!avatar.isEmpty()) {
            avatarService.uploadAvatar(id, avatar);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/{id}/db")
    public ResponseEntity<Avatar> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        System.out.println(avatar);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar);
    }

    @GetMapping(value = "/{id}/file")
    public ResponseEntity<Avatar> downloadAvatarLocal(@PathVariable Long id) {
        return ResponseEntity.ok(avatarService.findAvatar(id));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Avatar>> getAvatarsListFromPage(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) throws NullPointerException {
        try {
            if (avatarService.getAvatarsListFromPage(pageNumber, pageSize) != null) {
                return ResponseEntity.ok(avatarService.getAvatarsListFromPage(pageNumber, pageSize));
            }
            return ResponseEntity.ok(emptyList());
        } catch (NullPointerException n) {
            throw new NullPointerException("Avatars were not download in DataBase!");
        }
    }
}

