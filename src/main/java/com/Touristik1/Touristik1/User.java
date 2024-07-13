package com.Touristik1.Touristik1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

//нужно указать, что это именно модель, с которой мы будем авторизироваться
@Entity
@Table(name = "users")
@Data //чтобы не писать конструктор геттер, сеттер - lombok
@AllArgsConstructor //конструктор со всеми полями, которые присутствуют
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "active")
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id")) //primary key - user_id
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    //один пользователь ко многим товарам
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
    mappedBy = "user") //при удалении пользователя удаляем все связанные сущности
    private List<Tour> tours = new ArrayList<>();



    //security
    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
