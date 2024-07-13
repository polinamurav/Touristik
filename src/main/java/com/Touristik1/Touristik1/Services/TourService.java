package com.Touristik1.Touristik1.Services;

import com.Touristik1.Touristik1.Images;
import com.Touristik1.Touristik1.Tour;
import com.Touristik1.Touristik1.User;
import com.Touristik1.Touristik1.repositories.ImageRepository;
import com.Touristik1.Touristik1.repositories.TourRepository;
import javax.transaction.Transactional;

import com.Touristik1.Touristik1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

//сервис для работы с путевками. Любой класс, помеченный Service является компонентом
@Service
@Slf4j
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    //метод получения всего листа товаров. передаем все товары, которые присутствуют в памяти
    public List<Tour> listTours(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return tourRepository.findByCountryStartingWithIgnoreCase(keyword);
        }
        return tourRepository.findAll(); //нету страны - возвращаем все что есть
    }


    //нам не надо поочередно принимать поля title, description,... Spring может сам создать готовую модель.
    // Метод сохранения тура с изображением
    @Transactional
    public void saveTours(Principal principal, Tour tour, MultipartFile imageFile) throws IOException {
        //principal - обертка состояния у пользователя
        tour.setUser(getUserByPrincipal(principal));

        // Проверяем, что файл изображения передан
        if (imageFile != null && !imageFile.isEmpty()) {

            Images image = toImageEntity(imageFile); // Преобразуем MultipartFile в сущность Images
            tour.addImageToTour(image);
        }

        logTourInfo(tour);
        Tour savedTour = tourRepository.save(tour); // Сохраняем тур в базе данных
    }

    public User getUserByPrincipal(Principal principal) {
        //principal=всегда равен нулю в начале
        if(principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }

    // логирования информации о туре
    private void logTourInfo(Tour tour) {
        log.info("Saving new Tour. Stars: {}; Hotel_name: {};" +
                        "Country: {}; Fly_date: {}; night: {}; People: {}; Price: {}",
                tour.getStars(), tour.getHotel_name(), tour.getCountry(),
                tour.getFly_date(), tour.getNight(), tour.getPeople(), tour.getPrice());
    }

    private Images toImageEntity(MultipartFile file1) throws IOException {
        Images images = new Images();
        images.setName(file1.getName());
        images.setBytes(file1.getBytes());
        return images;
    }


    @Transactional
    public void updateTour(Tour tour, Long imageId, MultipartFile imageFile) throws IOException {
        // Проверяем, что файл изображения передан
        if (imageFile != null && !imageFile.isEmpty()) {

            Optional<Images> existingImageOptional = imageRepository.findById(imageId); // Проверяем, существует ли изображение с заданным id

            // Если изображение существует, обновляем его
            if (existingImageOptional.isPresent()) {
                Images existingImage = existingImageOptional.get();
                updateImage(existingImage, imageFile);
            } else {
                // Логика создания нового изображения или обработка ошибки
            }
        }

        logTourInfo(tour);

        tourRepository.save(tour);
    }



    private void updateImage(Images image, MultipartFile newImageFile) throws IOException {
        image.setBytes(newImageFile.getBytes()); // Обновляем содержимое изображения

        image.setName(newImageFile.getOriginalFilename());
    }

    //метод удаления
    public void deleteTour(Long id) {
        // Удаление связанных изображений
        imageRepository.deleteByTourId(id);

        // Удаление тура
        tourRepository.deleteById(id);
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id).orElse(null); //если товар с таким id не найден - вернет null
    }

    //сортировка путевок
    public List<Tour> sortTours(Sort.Direction direction) {
        return tourRepository.findAll(Sort.by(direction, "price"));
    }

}
