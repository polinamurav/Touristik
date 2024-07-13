package com.Touristik1.Touristik1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Touristik1.Touristik1.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername (String username);
}