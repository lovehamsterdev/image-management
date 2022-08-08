package com.example.imagemanagement.service.impl;

import com.example.imagemanagement.entity.Image;
import com.example.imagemanagement.exception.ImageNotFoundException;
import com.example.imagemanagement.repository.ImageRepository;
import com.example.imagemanagement.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> listFilenames() {
        return imageRepository.findAllFilenames();
    }

    @Override
    public Iterable<Image> saveAll(List<MultipartFile> files) {
        var images = files.stream()
                .filter(file -> !imageRepository.existsByFilename(file.getOriginalFilename()))
                .map(this::newImage)
                .collect(Collectors.toList());

        return imageRepository.saveAll(images);
    }

    @Override
    public int delete(String filename) {
        return imageRepository.deleteByFilename(filename);
    }

    @Override
    public Image replace(String filename, MultipartFile file) throws Exception {
        var image = imageRepository.findByFilename(filename)
                .orElseThrow(ImageNotFoundException::new);

        image.setMimeType(file.getContentType());
        image.setData(file.getBytes());

        return imageRepository.save(image);
    }

    private Image newImage(MultipartFile file) {
        try {
            return Image.builder()
                    .filename(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .data(file.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
