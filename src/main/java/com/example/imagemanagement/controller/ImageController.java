package com.example.imagemanagement.controller;

import com.example.imagemanagement.response.SaveResult;
import com.example.imagemanagement.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/images/db/{filename}")
    public ResponseEntity<Resource> retrieve(@PathVariable String filename) {
        var image = imageService.getImage(filename);
        var body = new ByteArrayResource(image.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, image.getMimeType())
                .cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)).cachePrivate().mustRevalidate())
                .body(body);
    }

    @PostMapping("/images/db/upload")
    public SaveResult upload(@RequestPart MultipartFile file) {
        try {
            var image = imageService.save(file);
            return SaveResult.builder()
                    .error(false)
                    .filename(image.getFilename())
                    .link(createImageLink(image.getFilename()))
                    .build();
        } catch (Exception e) {
            log.error("Failed to save image", e);
            return SaveResult.builder().error(true).filename(file.getOriginalFilename()).build();
        }
    }

    @PostMapping("/images/db/upload_multi")
    public List<SaveResult> uploadMulti(@RequestPart List<MultipartFile> files) {
        return files.stream()
                .map(this::upload)
                .collect(Collectors.toList());
    }

    private String createImageLink(String filename) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/images/db/" + filename)
                .toUriString();
    }
}
