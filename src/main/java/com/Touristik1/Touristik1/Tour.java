package com.Touristik1.Touristik1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tour") // Указывает имя таблицы в базе данных
@Data //чтобы не писать конструктор геттер, сеттер - lombok
@AllArgsConstructor //конструктор со всеми полями, которые присутствуют
@NoArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "stars")
    private int stars;
    @Column(name = "hotel_name")
    private String hotel_name;
    @Column(name = "country")
    private String country;
    @Column(name = "fly_date")
    private String fly_date;
    @Column(name = "night")
    private int night;
    @Column(name = "people")
    private int people;
    @Column(name = "price")
    private int price;


    private LocalDateTime dateOfCreated;
    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL)
    private Images images;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER) //при удалении пользователя не отразиться на товар
    @JoinColumn (name = "user_id")
    private User user;


    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    public void addImageToTour(Images images) {
        images.setTour(this);
        this.images = images;
    }
}
