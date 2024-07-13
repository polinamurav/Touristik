package com.Touristik1.Touristik1.repositories;

import com.Touristik1.Touristik1.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Images, Long> {

    void deleteByTourId(Long id);
}
