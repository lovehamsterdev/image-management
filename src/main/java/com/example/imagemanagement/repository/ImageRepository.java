package com.example.imagemanagement.repository;

import com.example.imagemanagement.entity.Image;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Integer> {
    Optional<Image> findByFilename(String filename);

    boolean existsByFilename(String filename);
}
