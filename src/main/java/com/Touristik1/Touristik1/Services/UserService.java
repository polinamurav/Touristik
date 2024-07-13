package com.Touristik1.Touristik1.Services;

import com.Touristik1.Touristik1.Role;
import com.Touristik1.Touristik1.User;
import com.Touristik1.Touristik1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser (User user) {
        String username = user.getUsername();
        if (userRepository.findByUsername(username) != null) return false;//такой user присутствует - ошибка значит
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword())); //шифрование пароля
        user.getRoles().add(Role.ROLE_USER);
        user.setName(user.getName());
        log.info("Saving new User with username: {}", username);
        userRepository.save(user);
        return true;
    }

    public List<User> list() {
        return userRepository.findAll();
    }


    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if(user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; username: {}", user.getId(), user.getUsername());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; username: {}", user.getId(), user.getUsername());
            }
        }
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username);
        }

        return null;
    }


    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

}
