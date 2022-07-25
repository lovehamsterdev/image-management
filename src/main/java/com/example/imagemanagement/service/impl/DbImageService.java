package com.example.imagemanagement.service.impl;

import com.example.imagemanagement.entity.Image;
import com.example.imagemanagement.exception.ImageNotFoundException;
import com.example.imagemanagement.repository.ImageRepository;
import com.example.imagemanagement.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class DbImageService implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public Image getImage(String filename) {
        return imageRepository.findByFilename(filename)
                .orElseThrow(ImageNotFoundException::new);
    }

    @Override
    public Image save(MultipartFile file) throws Exception {
        if (imageRepository.existsByFilename(file.getOriginalFilename())) {
            log.info("Image {} have already existed", file.getOriginalFilename());
            return Image.builder().filename(file.getOriginalFilename()).build();
        }

        var image = Image.builder()
                .filename(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .data(file.getBytes())
                .build();

        return imageRepository.save(image);
    }
}
