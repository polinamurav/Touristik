package com.Touristik1.Touristik1.repositories;

import com.Touristik1.Touristik1.Tour;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByCountryStartingWithIgnoreCase(String keyword);
}