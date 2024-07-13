package com.Touristik1.Touristik1.controllers;

import com.Touristik1.Touristik1.Images;
import com.Touristik1.Touristik1.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;

    @GetMapping("/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        Images images = imageRepository.findById(id).orElse(null);
        return ResponseEntity.ok()
                .body(new InputStreamResource(new ByteArrayInputStream(images.getBytes())));
    }
}
