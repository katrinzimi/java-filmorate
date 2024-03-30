package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.CheckDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Integer id;
    @NotBlank(message = "не должно быть пустым")
    private String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @CheckDate(message = "дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @PositiveOrZero(message = "продолжительность фильма должна быть положительной")
    private int duration;
    private Rating mpa;
    private List<Integer> likes = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();

}
