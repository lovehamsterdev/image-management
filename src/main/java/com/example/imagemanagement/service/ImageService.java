package com.example.imagemanagement.service;

import com.example.imagemanagement.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image getImage(String filename);

    Image save(MultipartFile file) throws Exception;
}
