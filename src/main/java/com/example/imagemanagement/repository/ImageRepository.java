package com.example.imagemanagement.repository;

import com.example.imagemanagement.entity.Image;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ImageRepository extends CrudRepository<Image, Integer> {
    Optional<Image> findByFilename(String filename);

    boolean existsByFilename(String filename);

    @Query("select filename from image")
    List<String> findAllFilenames();

    @Modifying
    @Transactional
    @Query("delete from image where filename = :filename")
    int deleteByFilename(@Param("filename") String filename);
}
