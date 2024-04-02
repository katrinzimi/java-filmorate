package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Genre> getGenreAll() {
        return genreStorage.getGenreAll();
    }

    @Override
    public Genre getGenreById(int id) {
        Genre result = genreStorage.getGenreById(id);
        if (result == null) {
            throw new NotFoundException("Жанр не найден");
        }
        return result;
    }


}
