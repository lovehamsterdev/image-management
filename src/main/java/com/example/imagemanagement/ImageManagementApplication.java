package com.example.imagemanagement;

import com.example.imagemanagement.entity.Image;
import com.example.imagemanagement.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class ImageManagementApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ImageManagementApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}
}
