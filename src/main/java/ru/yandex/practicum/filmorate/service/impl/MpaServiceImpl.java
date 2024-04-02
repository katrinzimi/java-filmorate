package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Mpa> getMpaAll() {
        return mpaStorage.getMpaAll();
    }

    @Override
    public Mpa getMpaById(int id) {
        Mpa result = mpaStorage.getMpaById(id);
        if (result == null) {
            throw new NotFoundException("MPA не найден");
        }
        return result;
    }

}
